package com.hisabak.feature.backup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hisabak.core.domain.AppPreferences
import com.hisabak.core.domain.analytics.Analytics
import com.hisabak.core.domain.analytics.AnalyticsEvent
import com.hisabak.core.domain.backup.AutoBackupPeriod
import com.hisabak.core.domain.backup.BackupPassphraseStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BackupUiState(
    val enabled: Boolean = false,
    val encryptionEnabled: Boolean = true,
    val passphraseSet: Boolean = false,
    val period: AutoBackupPeriod = AutoBackupPeriod.DEFAULT,
)

/**
 * Backup settings: enable Google Drive backup, set the encryption passphrase, and choose the
 * auto-backup period. The actual Drive upload/restore and auto-backup scheduling land in later PRs.
 */
class BackupViewModel(
    private val preferences: AppPreferences,
    private val passphraseStore: BackupPassphraseStore,
    private val analytics: Analytics,
) : ViewModel() {

    val state: StateFlow<BackupUiState> = combine(
        preferences.backupEnabled,
        preferences.backupEncryptionEnabled,
        passphraseStore.isSet,
        preferences.autoBackupPeriod,
    ) { enabled, encryptionEnabled, passphraseSet, period ->
        BackupUiState(
            enabled = enabled,
            encryptionEnabled = encryptionEnabled,
            passphraseSet = passphraseSet,
            period = period,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BackupUiState())

    fun setEnabled(enabled: Boolean) {
        analytics.log(AnalyticsEvent.BackupToggled(enabled))
        viewModelScope.launch {
            preferences.setBackupEnabled(enabled)
            // Disabling forgets the passphrase, so re-enabling asks for it again.
            if (!enabled) passphraseStore.clear()
        }
    }

    fun setEncryptionEnabled(enabled: Boolean) {
        analytics.log(AnalyticsEvent.BackupEncryptionToggled(enabled))
        viewModelScope.launch {
            preferences.setBackupEncryptionEnabled(enabled)
            // Turning encryption off discards the passphrase; it's re-asked when turned back on.
            if (!enabled) passphraseStore.clear()
        }
    }

    fun setPassphrase(passphrase: String) {
        viewModelScope.launch { passphraseStore.set(passphrase) }
    }

    fun setAutoBackupPeriod(period: AutoBackupPeriod) {
        analytics.log(AnalyticsEvent.AutoBackupPeriodSet(period.name.lowercase()))
        viewModelScope.launch { preferences.setAutoBackupPeriod(period) }
    }
}
