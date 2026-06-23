package com.hisabak.di

import com.hisabak.BuildConfig
import com.hisabak.core.data.backup.AesGcmBackupCrypto
import com.hisabak.core.data.backup.JsonBackupCodec
import com.hisabak.core.data.backup.KeystoreBackupPassphraseStore
import com.hisabak.core.data.backup.RoomBackupRepository
import com.hisabak.core.data.local.HisabakDatabase
import com.hisabak.core.domain.backup.BackupCodec
import com.hisabak.core.domain.backup.BackupCrypto
import com.hisabak.core.domain.backup.BackupPassphraseStore
import com.hisabak.core.domain.backup.BackupRepository
import com.hisabak.core.domain.backup.ExportBackupUseCase
import com.hisabak.core.domain.backup.ImportBackupUseCase
import com.hisabak.feature.backup.presentation.BackupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val backupModule = module {
    single<BackupPassphraseStore> { KeystoreBackupPassphraseStore(androidContext()) }
    viewModel { BackupViewModel(preferences = get(), passphraseStore = get(), analytics = get()) }

    // Encryption engine — destination-agnostic foundation for the deferred Google Drive sync.
    single<BackupRepository> {
        RoomBackupRepository(
            db = get(),
            categoryDao = get(),
            categoryLimitDao = get(),
            brandDao = get(),
            transactionDao = get(),
            smsDao = get(),
        )
    }
    single<BackupCodec> { JsonBackupCodec() }
    single<BackupCrypto> { AesGcmBackupCrypto() }
    factory {
        ExportBackupUseCase(
            repository = get(),
            codec = get(),
            crypto = get(),
            clock = get(),
            appVersionCode = BuildConfig.VERSION_CODE,
            schemaVersion = HisabakDatabase.SCHEMA_VERSION,
        )
    }
    factory {
        ImportBackupUseCase(
            repository = get(),
            codec = get(),
            crypto = get(),
            schemaVersion = HisabakDatabase.SCHEMA_VERSION,
        )
    }
}
