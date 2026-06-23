package com.hisabak.core.domain.backup

import com.hisabak.core.common.Clock

sealed interface ExportResult {
    /** The encrypted backup bytes — the caller writes them wherever (SAF file now, Drive later). */
    data class Success(val bytes: ByteArray) : ExportResult
    data class Failure(val error: BackupError) : ExportResult
}

class ExportBackupUseCase(
    private val repository: BackupRepository,
    private val codec: BackupCodec,
    private val crypto: BackupCrypto,
    private val clock: Clock,
    private val appVersionCode: Int,
    private val schemaVersion: Int,
) {
    suspend operator fun invoke(passphrase: String): ExportResult = try {
        val data = repository.snapshot()
        if (data.totalRecords == 0) {
            ExportResult.Failure(BackupError.Empty)
        } else {
            val envelope = BackupEnvelope(
                formatVersion = BACKUP_FORMAT_VERSION,
                schemaVersion = schemaVersion,
                appVersionCode = appVersionCode,
                createdAtMillis = clock.now().toEpochMilli(),
                data = data,
            )
            ExportResult.Success(crypto.encrypt(codec.encode(envelope), passphrase))
        }
    } catch (e: BackupException) {
        ExportResult.Failure(e.error)
    }
}
