package com.hisabak.core.data.backup

import com.hisabak.core.domain.backup.BackupCrypto
import com.hisabak.core.domain.backup.BackupError
import com.hisabak.core.domain.backup.BackupException
import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Passphrase-based AES-256-GCM encryption for backup bytes. The passphrase is the portable root of
 * trust (works across devices); there is no recovery if it's lost — by design. File layout is a
 * plaintext header (magic + format + KDF iterations + salt + IV) followed by the GCM ciphertext,
 * whose auth tag detects both a wrong passphrase and tampering.
 */
class AesGcmBackupCrypto(
    private val random: SecureRandom = SecureRandom(),
) : BackupCrypto {

    override fun encrypt(plaintext: ByteArray, passphrase: String): ByteArray {
        val salt = ByteArray(SALT_LEN).also(random::nextBytes)
        val iv = ByteArray(IV_LEN).also(random::nextBytes)
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, deriveKey(passphrase, salt, ITERATIONS), GCMParameterSpec(TAG_BITS, iv))
        }
        val body = cipher.doFinal(plaintext)
        return ByteBuffer.allocate(MAGIC.size + 1 + 4 + SALT_LEN + IV_LEN + body.size)
            .put(MAGIC)
            .put(FORMAT.toByte())
            .putInt(ITERATIONS)
            .put(salt)
            .put(iv)
            .put(body)
            .array()
    }

    override fun decrypt(ciphertext: ByteArray, passphrase: String): ByteArray {
        try {
            val buffer = ByteBuffer.wrap(ciphertext)
            val magic = ByteArray(MAGIC.size).also(buffer::get)
            if (!magic.contentEquals(MAGIC) || buffer.get().toInt() != FORMAT) {
                throw BackupException(BackupError.Corrupt)
            }
            val iterations = buffer.int
            val salt = ByteArray(SALT_LEN).also(buffer::get)
            val iv = ByteArray(IV_LEN).also(buffer::get)
            val body = ByteArray(buffer.remaining()).also(buffer::get)
            val cipher = Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, deriveKey(passphrase, salt, iterations), GCMParameterSpec(TAG_BITS, iv))
            }
            return cipher.doFinal(body)
        } catch (e: AEADBadTagException) {
            throw BackupException(BackupError.WrongPassphrase)
        } catch (e: BackupException) {
            throw e
        } catch (e: Exception) {
            throw BackupException(BackupError.Corrupt)
        }
    }

    private fun deriveKey(passphrase: String, salt: ByteArray, iterations: Int): SecretKeySpec {
        val spec = PBEKeySpec(passphrase.toCharArray(), salt, iterations, KEY_BITS)
        val bytes = SecretKeyFactory.getInstance(KDF).generateSecret(spec).encoded
        return SecretKeySpec(bytes, "AES")
    }

    private companion object {
        val MAGIC = "HSBK".toByteArray(Charsets.US_ASCII)
        const val FORMAT = 1
        const val SALT_LEN = 16
        const val IV_LEN = 12
        const val TAG_BITS = 128
        const val KEY_BITS = 256
        const val ITERATIONS = 210_000
        const val KDF = "PBKDF2WithHmacSHA256"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
    }
}
