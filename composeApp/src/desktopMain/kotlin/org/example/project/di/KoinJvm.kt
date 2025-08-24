package org.example.project.di

import org.example.project.dbFactory.DatabaseFactory
import org.example.project.preferences.MultiplatformSettingsFactory
import org.koin.dsl.module

val jvmModules = module {
    single { DatabaseFactory() }
    single { MultiplatformSettingsFactory() }
}

fun initKoinJvm() = initKoin(additionalModule = listOf(jvmModules))