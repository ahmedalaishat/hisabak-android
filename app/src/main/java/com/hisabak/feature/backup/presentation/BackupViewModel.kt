package com.hisabak.feature.backup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hisabak.core.domain.analytics.Analytics
import com.hisabak.core.domain.analytics.AnalyticsEvent
import com.hisabak.core.domain.backup.BackupError
import com.hisabak.core.domain.backup.ExportBackupUseCase
import com.hisabak.core.domain.backup.ExportResult
import com.hisabak.core.domain.backup.ImportBackupUseCase
import com.hisabak.core.domain.backup.ImportResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Outcome of the last export/import, surfaced to the UI as a one-shot banner. */
sealed interface BackupOutcome {
    data object Exported : BackupOutcome
    data class Imported(val records: Int) : BackupOutcome
    data class Failed(val error: BackupError) : BackupOutcome

    /** Reading/writing the chosen file failed (the data layer never ran). */
    data object FileError : BackupOutcome
}

data class BackupUiState(
    val busy: Boolean = false,
    val result: BackupOutcome? = null,
)

/**
 * Drives export/import. Stays destination-agnostic: the caller supplies a byte sink/source
 * (a SAF file today, Google Drive later), so this never touches a `Uri` or `ContentResolver`.
 */
class BackupViewModel(
    private val exportBackup: ExportBackupUseCase,
    private val importBackup: ImportBackupUseCase,
    private val analytics: Analytics,
) : ViewModel() {

    private val _state = MutableStateFlow(BackupUiState())
    val state: StateFlow<BackupUiState> = _state.asStateFlow()

    fun export(passphrase: String, writeBytes: suspend (ByteArray) -> Unit) {
        if (_state.value.busy) return
        viewModelScope.launch {
            _state.value = BackupUiState(busy = true)
            val outcome = when (val result = exportBackup(passphrase)) {
                is ExportResult.Success -> try {
                    writeBytes(result.bytes)
                    analytics.log(AnalyticsEvent.BackupExported(true))
                    BackupOutcome.Exported
                } catch (e: Exception) {
                    analytics.log(AnalyticsEvent.BackupExported(false))
                    BackupOutcome.FileError
                }
                is ExportResult.Failure -> {
                    analytics.log(AnalyticsEvent.BackupExported(false))
                    BackupOutcome.Failed(result.error)
                }
            }
            _state.value = BackupUiState(result = outcome)
        }
    }

    fun import(passphrase: String, readBytes: suspend () -> ByteArray) {
        if (_state.value.busy) return
        viewModelScope.launch {
            _state.value = BackupUiState(busy = true)
            val bytes = try {
                readBytes()
            } catch (e: Exception) {
                null
            }
            val outcome = if (bytes == null) {
                analytics.log(AnalyticsEvent.BackupImported(false))
                BackupOutcome.FileError
            } else when (val result = importBackup(bytes, passphrase)) {
                is ImportResult.Success -> {
                    analytics.log(AnalyticsEvent.BackupImported(true))
                    BackupOutcome.Imported(result.restoredRecords)
                }
                is ImportResult.Failure -> {
                    analytics.log(AnalyticsEvent.BackupImported(false))
                    BackupOutcome.Failed(result.error)
                }
            }
            _state.value = BackupUiState(result = outcome)
        }
    }

    fun clearResult() {
        _state.value = _state.value.copy(result = null)
    }
}
