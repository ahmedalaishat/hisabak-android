package com.hisabak.di

import androidx.room.Room
import com.hisabak.BuildConfig
import com.hisabak.core.data.local.DatabaseSeeder
import com.hisabak.core.data.local.HisabakDatabase
import com.hisabak.core.data.local.MIGRATION_1_2
import com.hisabak.core.data.local.security.DatabaseEncryptionMigration
import com.hisabak.core.data.local.security.KeystoreDatabaseKeyStore
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { KeystoreDatabaseKeyStore(androidContext()) }
    single {
        // The database is encrypted at rest with SQLCipher. The passphrase is a random secret
        // wrapped in the Android Keystore; existing plaintext databases are migrated in place
        // (once) before Room opens this file.
        System.loadLibrary("sqlcipher")
        val passphrase = get<KeystoreDatabaseKeyStore>().getOrCreatePassphrase()
        DatabaseEncryptionMigration.migrateIfPlaintext(androidContext(), passphrase)
        Room.databaseBuilder(
            context = androidContext(),
            klass = HisabakDatabase::class.java,
            name = HisabakDatabase.NAME,
        )
            .openHelperFactory(SupportOpenHelperFactory(passphrase))
            .addMigrations(MIGRATION_1_2)
            // Destructive fallback only in debug builds, for fast schema iteration. Release
            // builds must ship a real migration — a missing one fails loudly rather than
            // silently wiping the user's on-device financial history.
            .apply { if (BuildConfig.DEBUG) fallbackToDestructiveMigration(dropAllTables = true) }
            .build()
    }
    single { get<HisabakDatabase>().categoryDao() }
    single { get<HisabakDatabase>().categoryLimitDao() }
    single { get<HisabakDatabase>().brandDao() }
    single { get<HisabakDatabase>().transactionDao() }
    single { get<HisabakDatabase>().smsDao() }
    single { get<HisabakDatabase>().notificationDao() }
    single { get<HisabakDatabase>().categoryLimitAlertDao() }
    single { DatabaseSeeder(db = get(), seed = get(), starters = get(), currency = get()) }
}
