package com.hisabak

import android.app.Application
import com.hisabak.di.AppContainer

class HisabakApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
