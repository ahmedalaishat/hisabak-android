package com.hisabak.feature.restore.presentation

import com.hisabak.core.data.backup.AesGcmBackupCrypto
import com.hisabak.core.data.backup.AuthorizeOutcome
import com.hisabak.core.data.backup.JsonBackupCodec
import com.hisabak.core.domain.backup.RestoreFromRemoteUseCase
import com.hisabak.core.domain.backup.RunBackupUseCase
import com.hisabak.testutil.FakeAnalytics
import com.hisabak.testutil.FakeAppPreferences
import com.hisabak.testutil.FakeBackupAccountStore
import com.hisabak.testutil.FakeBackupRemote
import com.hisabak.testutil.FakeBackupRepository
import com.hisabak.testutil.FakeDriveAuthorizer
import com.hisabak.testutil.MainDispatcherRule
import com.hisabak.testutil.TestClock
import com.hisabak.testutil.sampleBackupData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RestoreViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val codec = JsonBackupCodec()
    private val crypto = AesGcmBackupCrypto()

    private fun viewModel(
        remote: FakeBackupRemote,
        target: FakeBackupRepository,
        prefs: FakeAppPreferences = FakeAppPreferences(),
        analytics: FakeAnalytics = FakeAnalytics(),
    ): RestoreViewModel {
        val restore = RestoreFromRemoteUseCase(target, codec, crypto, remote, schemaVersion = 2)
        return RestoreViewModel(restore, FakeDriveAuthorizer(), FakeBackupAccountStore(), prefs, analytics)
    }

    private suspend fun seedEncryptedBackup(remote: FakeBackupRemote, passphrase: String) {
        RunBackupUseCase(FakeBackupRepository(sampleBackupData()), codec, crypto, remote, TestClock(), 8, 2)
            .invoke(passphrase)
    }

    @Test
    fun `connect then passphrase restores and marks the offer done`() = runTest {
        val remote = FakeBackupRemote()
        seedEncryptedBackup(remote, "pass1234")
        val target = FakeBackupRepository()
        val prefs = FakeAppPreferences()
        val vm = viewModel(remote, target, prefs)

        vm.connect(onNeedConsent = {})
        advanceUntilIdle()
        assertTrue(vm.state.value.needsPassphrase) // encrypted → prompt

        vm.submitPassphrase("pass1234")
        advanceUntilIdle()

        assertEquals(sampleBackupData(), target.replacedWith)
        assertTrue(prefs.restoreOffered.first())
    }

    @Test
    fun `no backup for the account reports nothing found`() = runTest {
        val vm = viewModel(FakeBackupRemote(), FakeBackupRepository())
        vm.connect(onNeedConsent = {})
        advanceUntilIdle()
        assertEquals(RestoreMessage.NothingFound, vm.state.value.message)
    }

    @Test
    fun `skip marks the offer done without restoring`() = runTest {
        val target = FakeBackupRepository()
        val prefs = FakeAppPreferences()
        val vm = viewModel(FakeBackupRemote(), target, prefs)

        vm.skip()
        advanceUntilIdle()

        assertTrue(prefs.restoreOffered.first())
        assertFalse(target.replacedWith != null)
    }
}
