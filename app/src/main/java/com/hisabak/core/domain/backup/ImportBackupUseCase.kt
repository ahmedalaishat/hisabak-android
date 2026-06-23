package com.hisabak.core.domain.backup

sealed interface ImportResult {
    data class Success(val restoredRecords: Int) : ImportResult
    data class Failure(val error: BackupError) : ImportResult
}

class ImportBackupUseCase(
    private val repository: BackupRepository,
    private val codec: BackupCodec,
    private val crypto: BackupCrypto,
    private val schemaVersion: Int,
) {
    suspend operator fun invoke(bytes: ByteArray, passphrase: String): ImportResult = try {
        if (bytes.isEmpty()) {
            ImportResult.Failure(BackupError.Empty)
        } else {
            val envelope = codec.decode(crypto.decrypt(bytes, passphrase))
            if (envelope.schemaVersion > schemaVersion) {
                ImportResult.Failure(BackupError.UnsupportedVersion(envelope.schemaVersion, schemaVersion))
            } else {
                // Older backups decode forward via record defaults; no upgrade step needed yet.
                repository.replaceAll(envelope.data)
                ImportResult.Success(envelope.data.totalRecords)
            }
        }
    } catch (e: BackupException) {
        ImportResult.Failure(e.error)
    }
}
