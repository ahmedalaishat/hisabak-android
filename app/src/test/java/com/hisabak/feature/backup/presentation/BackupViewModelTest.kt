package com.hisabak.feature.backup.presentation

import app.cash.turbine.test
import com.hisabak.core.domain.backup.AutoBackupPeriod
import com.hisabak.testutil.FakeAnalytics
import com.hisabak.testutil.FakeAppPreferences
import com.hisabak.testutil.FakeBackupPassphraseStore
import com.hisabak.testutil.MainDispatcherRule
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
class BackupViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModel(
        prefs: FakeAppPreferences = FakeAppPreferences(),
        store: FakeBackupPassphraseStore = FakeBackupPassphraseStore(),
        analytics: FakeAnalytics = FakeAnalytics(),
    ) = BackupViewModel(prefs, store, analytics)

    @Test
    fun `enabling persists the flag and logs it`() = runTest {
        val prefs = FakeAppPreferences()
        val analytics = FakeAnalytics()
        val vm = viewModel(prefs = prefs, analytics = analytics)

        vm.setEnabled(true)
        advanceUntilIdle()

        assertTrue(prefs.backupEnabled.first())
        assertEquals(listOf("backup_toggled"), analytics.names())
    }

    @Test
    fun `disabling clears the passphrase`() = runTest {
        val store = FakeBackupPassphraseStore().apply { set("secret123") }
        val prefs = FakeAppPreferences().apply { setBackupEnabled(true) }
        val vm = viewModel(prefs = prefs, store = store)

        vm.setEnabled(false)
        advanceUntilIdle()

        assertFalse(prefs.backupEnabled.first())
        assertEquals(null, store.get())
    }

    @Test
    fun `turning encryption off clears the passphrase and logs it`() = runTest {
        val store = FakeBackupPassphraseStore().apply { set("secret123") }
        val prefs = FakeAppPreferences().apply { setBackupEncryptionEnabled(true) }
        val analytics = FakeAnalytics()
        val vm = viewModel(prefs = prefs, store = store, analytics = analytics)

        vm.setEncryptionEnabled(false)
        advanceUntilIdle()

        assertFalse(prefs.backupEncryptionEnabled.first())
        assertEquals(null, store.get())
        assertEquals(listOf("backup_encryption_toggled"), analytics.names())
    }

    @Test
    fun `setting the passphrase stores it and surfaces in state`() = runTest {
        val store = FakeBackupPassphraseStore()
        val vm = viewModel(store = store)

        vm.state.test {
            assertFalse(awaitItem().passphraseSet)
            vm.setPassphrase("correct horse")
            advanceUntilIdle()
            assertTrue(awaitItem().passphraseSet)
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals("correct horse", store.get())
    }

    @Test
    fun `setting the auto-backup period persists and logs it`() = runTest {
        val prefs = FakeAppPreferences()
        val analytics = FakeAnalytics()
        val vm = viewModel(prefs = prefs, analytics = analytics)

        vm.setAutoBackupPeriod(AutoBackupPeriod.MONTHLY)
        advanceUntilIdle()

        assertEquals(AutoBackupPeriod.MONTHLY, prefs.autoBackupPeriod.first())
        assertEquals(listOf("auto_backup_period_set"), analytics.names())
    }
}
