package com.hisabak.di

import androidx.room.Room
import com.hisabak.BuildConfig
import com.hisabak.core.data.local.DatabaseSeeder
import com.hisabak.core.data.local.HisabakDatabase
import com.hisabak.core.data.local.MIGRATION_1_2
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = HisabakDatabase::class.java,
            name = HisabakDatabase.NAME,
        )
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
    single { DatabaseSeeder(db = get(), seed = get(), currency = get()) }
}
