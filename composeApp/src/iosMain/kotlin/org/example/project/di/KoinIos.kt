package org.example.project.di

import org.example.project.dbFactory.DatabaseFactory
import org.example.project.di.initKoin
import org.example.project.preferences.MultiplatformSettingsFactory
import org.koin.dsl.module

val iosModules = module {
    single { DatabaseFactory() }
    single { MultiplatformSettingsFactory() }

}

fun initKoinIOS() = initKoin(additionalModule = listOf(iosModules))