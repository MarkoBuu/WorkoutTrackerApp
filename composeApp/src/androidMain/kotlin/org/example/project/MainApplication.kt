package org.example.project

import android.app.Application
import org.example.project.dbFactory.DatabaseFactory
import org.example.project.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class MainApplication : Application() {

    private val androidModules = module {
        single { DatabaseFactory(applicationContext) }
    }

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        initKoin(additionalModule = listOf(androidModules)) {
            androidContext(applicationContext)
        }
    }
}