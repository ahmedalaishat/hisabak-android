package com.hisabak.core.domain.backup

import com.hisabak.core.data.backup.AesGcmBackupCrypto
import com.hisabak.core.data.backup.JsonBackupCodec
import com.hisabak.testutil.FakeBackupRepository
import com.hisabak.testutil.TestClock
import com.hisabak.testutil.sampleBackupData
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupUseCasesTest {

    private val codec = JsonBackupCodec()
    private val crypto = AesGcmBackupCrypto()
    private val schemaVersion = 2

    private fun exporter(repo: FakeBackupRepository, schema: Int = schemaVersion) =
        ExportBackupUseCase(repo, codec, crypto, TestClock(), appVersionCode = 8, schemaVersion = schema)

    private fun importer(repo: FakeBackupRepository, schema: Int = schemaVersion) =
        ImportBackupUseCase(repo, codec, crypto, schemaVersion = schema)

    @Test
    fun `export then import reproduces the snapshot`() = runTest {
        val source = FakeBackupRepository(sampleBackupData())
        val bytes = (exporter(source).invoke("pass1234") as ExportResult.Success).bytes

        val target = FakeBackupRepository()
        val result = importer(target).invoke(bytes, "pass1234")

        assertEquals(ImportResult.Success(sampleBackupData().totalRecords), result)
        assertEquals(sampleBackupData(), target.replacedWith)
    }

    @Test
    fun `exporting with no data fails as Empty`() = runTest {
        val result = exporter(FakeBackupRepository(BackupData())).invoke("pass1234")
        assertEquals(ExportResult.Failure(BackupError.Empty), result)
    }

    @Test
    fun `import with wrong passphrase fails and does not touch data`() = runTest {
        val bytes = (exporter(FakeBackupRepository(sampleBackupData())).invoke("right-one") as ExportResult.Success).bytes
        val target = FakeBackupRepository()

        val result = importer(target).invoke(bytes, "wrong-one")

        assertEquals(ImportResult.Failure(BackupError.WrongPassphrase), result)
        assertTrue(target.replacedWith == null)
    }

    @Test
    fun `import of a newer schema is rejected`() = runTest {
        // Export stamped one schema version ahead of what this app supports.
        val bytes = (exporter(FakeBackupRepository(sampleBackupData()), schema = schemaVersion + 1)
            .invoke("pass1234") as ExportResult.Success).bytes
        val target = FakeBackupRepository()

        val result = importer(target, schema = schemaVersion).invoke(bytes, "pass1234")

        assertEquals(
            ImportResult.Failure(BackupError.UnsupportedVersion(schemaVersion + 1, schemaVersion)),
            result,
        )
        assertTrue(target.replacedWith == null)
    }
}
