package com.hisabak.feature.backup.presentation

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hisabak.core.data.backup.AuthorizeOutcome
import com.hisabak.core.data.backup.DriveAuthorizer
import com.hisabak.core.domain.AppPreferences
import com.hisabak.core.domain.analytics.Analytics
import com.hisabak.core.domain.analytics.AnalyticsEvent
import com.hisabak.core.domain.backup.AutoBackupPeriod
import com.hisabak.core.domain.backup.BackupAccount
import com.hisabak.core.domain.backup.BackupAccountStore
import com.hisabak.core.domain.backup.BackupError
import com.hisabak.core.domain.backup.BackupPassphraseStore
import com.hisabak.core.domain.backup.BackupRunResult
import com.hisabak.core.domain.backup.RunBackupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface BackupMessage {
    data object BackedUp : BackupMessage
    data class Failed(val error: BackupError) : BackupMessage
}

data class BackupUiState(
    val enabled: Boolean = false,
    val encryptionEnabled: Boolean = true,
    val passphraseSet: Boolean = false,
    val period: AutoBackupPeriod = AutoBackupPeriod.DEFAULT,
    val account: BackupAccount? = null,
    val busy: Boolean = false,
    val message: BackupMessage? = null,
)

private data class Settings(
    val enabled: Boolean,
    val encryptionEnabled: Boolean,
    val passphraseSet: Boolean,
    val period: AutoBackupPeriod,
)

private data class Transient(val busy: Boolean = false, val message: BackupMessage? = null)

class BackupViewModel(
    private val preferences: AppPreferences,
    private val passphraseStore: BackupPassphraseStore,
    private val accountStore: BackupAccountStore,
    private val authorizer: DriveAuthorizer,
    private val runBackup: RunBackupUseCase,
    private val analytics: Analytics,
) : ViewModel() {

    private val transient = MutableStateFlow(Transient())

    val state: StateFlow<BackupUiState> = combine(
        combine(
            preferences.backupEnabled,
            preferences.backupEncryptionEnabled,
            passphraseStore.isSet,
            preferences.autoBackupPeriod,
        ) { enabled, encryption, passphraseSet, period -> Settings(enabled, encryption, passphraseSet, period) },
        accountStore.account,
        transient,
    ) { s, account, t ->
        BackupUiState(s.enabled, s.encryptionEnabled, s.passphraseSet, s.period, account, t.busy, t.message)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BackupUiState())

    fun setEnabled(enabled: Boolean) {
        analytics.log(AnalyticsEvent.BackupToggled(enabled))
        viewModelScope.launch {
            preferences.setBackupEnabled(enabled)
            if (!enabled) passphraseStore.clear()
        }
    }

    fun setEncryptionEnabled(enabled: Boolean) {
        analytics.log(AnalyticsEvent.BackupEncryptionToggled(enabled))
        viewModelScope.launch {
            preferences.setBackupEncryptionEnabled(enabled)
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

    /** Begins the account connect flow; [onNeedConsent] launches the consent UI for a result. */
    fun connect(onNeedConsent: (IntentSender) -> Unit) {
        viewModelScope.launch {
            when (val outcome = authorizer.authorize()) {
                is AuthorizeOutcome.Granted -> connected(outcome.account)
                is AuthorizeOutcome.NeedsConsent -> onNeedConsent(outcome.intentSender)
                AuthorizeOutcome.Failed -> fail(BackupError.AuthRequired)
            }
        }
    }

    fun onConsentResult(data: Intent?) {
        viewModelScope.launch {
            when (val outcome = authorizer.resultFrom(data)) {
                is AuthorizeOutcome.Granted -> connected(outcome.account)
                else -> fail(BackupError.AuthRequired)
            }
        }
    }

    fun backupNow() {
        viewModelScope.launch {
            if (accountStore.account.first() == null) {
                fail(BackupError.AuthRequired)
                return@launch
            }
            val passphrase = if (preferences.backupEncryptionEnabled.first()) {
                passphraseStore.get() ?: run { fail(BackupError.PassphraseRequired); return@launch }
            } else {
                null
            }
            transient.update { it.copy(busy = true, message = null) }
            val result = runBackup(passphrase)
            analytics.log(AnalyticsEvent.BackupRunCompleted(result is BackupRunResult.Success))
            transient.value = Transient(
                busy = false,
                message = when (result) {
                    BackupRunResult.Success -> BackupMessage.BackedUp
                    is BackupRunResult.Failure -> BackupMessage.Failed(result.error)
                },
            )
        }
    }

    fun clearMessage() = transient.update { it.copy(message = null) }

    private suspend fun connected(account: BackupAccount) {
        accountStore.set(account)
        analytics.log(AnalyticsEvent.BackupAccountConnected)
    }

    private fun fail(error: BackupError) = transient.update { it.copy(busy = false, message = BackupMessage.Failed(error)) }
}
