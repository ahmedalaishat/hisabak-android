package com.hisabak.core.data.local.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.ByteBuffer
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Holds the passphrase used to encrypt the SQLCipher database at rest. A random 256-bit secret is
 * generated on first use, wrapped with a non-exportable Android Keystore AES key, and only the
 * IV + ciphertext are persisted (never the plaintext). The key is **not** auth-gated, so the
 * database can be opened on a cold start and by the unattended background backup worker; the
 * live-device threat is covered by App Lock, not by this key.
 *
 * Reads are synchronous because the passphrase is needed at `Room.databaseBuilder` time, before any
 * coroutine context exists — backed by SharedPreferences rather than DataStore for that reason.
 */
class KeystoreDatabaseKeyStore(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** Returns the stored database passphrase, generating and persisting one on first call. */
    @Synchronized
    fun getOrCreatePassphrase(): ByteArray {
        prefs.getString(KEY_CIPHERTEXT, null)?.let { return unwrap(it) }
        val secret = ByteArray(SECRET_LEN).also { SecureRandom().nextBytes(it) }
        val passphrase = Base64.encode(secret, Base64.NO_WRAP)
        prefs.edit().putString(KEY_CIPHERTEXT, wrap(passphrase)).apply()
        return passphrase
    }

    private fun wrap(plaintext: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply { init(Cipher.ENCRYPT_MODE, secretKey()) }
        val body = cipher.doFinal(plaintext)
        val packed = ByteBuffer.allocate(1 + cipher.iv.size + body.size)
            .put(cipher.iv.size.toByte())
            .put(cipher.iv)
            .put(body)
            .array()
        return Base64.encodeToString(packed, Base64.NO_WRAP)
    }

    private fun unwrap(stored: String): ByteArray {
        val buffer = ByteBuffer.wrap(Base64.decode(stored, Base64.NO_WRAP))
        val iv = ByteArray(buffer.get().toInt()).also(buffer::get)
        val body = ByteArray(buffer.remaining()).also(buffer::get)
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, secretKey(), GCMParameterSpec(TAG_BITS, iv))
        }
        return cipher.doFinal(body)
    }

    private fun secretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        (keyStore.getEntry(ALIAS, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        generator.init(
            KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build(),
        )
        return generator.generateKey()
    }

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val ALIAS = "hisabak_database_key"
        const val PREFS_NAME = "hisabak_db_key"
        const val KEY_CIPHERTEXT = "db_passphrase_enc"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val SECRET_LEN = 32
        const val TAG_BITS = 128
    }
}
