package com.hisabak.core.data.backup

import com.hisabak.core.domain.backup.BackupError
import com.hisabak.core.domain.backup.BackupException
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Test

class AesGcmBackupCryptoTest {

    private val crypto = AesGcmBackupCrypto()
    private val plaintext = "the quick brown fox spends AED 1,234".toByteArray()

    @Test
    fun `encrypt then decrypt round-trips`() {
        val out = crypto.decrypt(crypto.encrypt(plaintext, "correct horse"), "correct horse")
        assertArrayEquals(plaintext, out)
    }

    @Test
    fun `ciphertext does not contain the plaintext`() {
        val cipher = crypto.encrypt(plaintext, "pw")
        assertFalse(cipher.toString(Charsets.ISO_8859_1).contains("brown fox"))
    }

    @Test
    fun `wrong passphrase fails with WrongPassphrase`() {
        val cipher = crypto.encrypt(plaintext, "right")
        val e = assertThrows(BackupException::class.java) { crypto.decrypt(cipher, "wrong") }
        assertEquals(BackupError.WrongPassphrase, e.error)
    }

    @Test
    fun `garbage input fails with Corrupt`() {
        val e = assertThrows(BackupException::class.java) { crypto.decrypt(byteArrayOf(1, 2, 3), "pw") }
        assertEquals(BackupError.Corrupt, e.error)
    }
}
