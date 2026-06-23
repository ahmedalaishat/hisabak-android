package com.hisabak.feature.backup.presentation

import com.hisabak.core.data.backup.AesGcmBackupCrypto
import com.hisabak.core.data.backup.JsonBackupCodec
import com.hisabak.core.domain.backup.ExportBackupUseCase
import com.hisabak.core.domain.backup.ExportResult
import com.hisabak.core.domain.backup.ImportBackupUseCase
import com.hisabak.testutil.FakeAnalytics
import com.hisabak.testutil.FakeBackupRepository
import com.hisabak.testutil.MainDispatcherRule
import com.hisabak.testutil.TestClock
import com.hisabak.testutil.sampleBackupData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BackupViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val codec = JsonBackupCodec()
    private val crypto = AesGcmBackupCrypto()

    private fun viewModel(repo: FakeBackupRepository, analytics: FakeAnalytics) = BackupViewModel(
        exportBackup = ExportBackupUseCase(repo, codec, crypto, TestClock(), 8, 2),
        importBackup = ImportBackupUseCase(repo, codec, crypto, 2),
        analytics = analytics,
    )

    @Test
    fun `export success writes bytes and reports Exported`() = runTest {
        val analytics = FakeAnalytics()
        val vm = viewModel(FakeBackupRepository(sampleBackupData()), analytics)
        var written: ByteArray? = null

        vm.export("pass1234") { written = it }
        advanceUntilIdle()

        assertEquals(BackupOutcome.Exported, vm.state.value.result)
        assertTrue(written != null && written!!.isNotEmpty())
        assertEquals(listOf("backup_exported"), analytics.names())
    }

    @Test
    fun `export reports FileError when writing fails`() = runTest {
        val analytics = FakeAnalytics()
        val vm = viewModel(FakeBackupRepository(sampleBackupData()), analytics)

        vm.export("pass1234") { throw java.io.IOException("disk full") }
        advanceUntilIdle()

        assertEquals(BackupOutcome.FileError, vm.state.value.result)
        assertEquals(listOf("backup_exported"), analytics.names())
    }

    @Test
    fun `import success reports the restored count`() = runTest {
        val source = FakeBackupRepository(sampleBackupData())
        val bytes = (ExportBackupUseCase(source, codec, crypto, TestClock(), 8, 2)
            .invoke("pass1234") as ExportResult.Success).bytes
        val analytics = FakeAnalytics()
        val vm = viewModel(FakeBackupRepository(), analytics)

        vm.import("pass1234") { bytes }
        advanceUntilIdle()

        assertEquals(BackupOutcome.Imported(sampleBackupData().totalRecords), vm.state.value.result)
        assertEquals(listOf("backup_imported"), analytics.names())
    }

    @Test
    fun `import with the wrong passphrase reports failure`() = runTest {
        val source = FakeBackupRepository(sampleBackupData())
        val bytes = (ExportBackupUseCase(source, codec, crypto, TestClock(), 8, 2)
            .invoke("right-one") as ExportResult.Success).bytes
        val analytics = FakeAnalytics()
        val vm = viewModel(FakeBackupRepository(), analytics)

        vm.import("wrong-one") { bytes }
        advanceUntilIdle()

        assertTrue(vm.state.value.result is BackupOutcome.Failed)
        assertEquals(listOf("backup_imported"), analytics.names())
    }
}
