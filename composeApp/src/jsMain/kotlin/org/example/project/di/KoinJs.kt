package org.example.project.di

import org.example.project.dbFactory.DatabaseFactory
import org.example.project.di.initKoin
import org.example.project.preferences.MultiplatformSettingsFactory
import org.koin.dsl.module

val jsModules = module {
    single { DatabaseFactory() }
    single { MultiplatformSettingsFactory() }
}

fun initKoinJs() = initKoin(additionalModule = listOf(jsModules))