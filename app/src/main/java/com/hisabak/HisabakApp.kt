package com.hisabak

import android.app.Application
import com.hisabak.core.data.local.DatabaseSeeder
import com.hisabak.di.appModules
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class HisabakApp : Application() {

    private val seeder: DatabaseSeeder by inject()
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@HisabakApp)
            modules(appModules)
        }
        appScope.launch { seeder.seedIfEmpty() }
    }
}
