package org.example.project.di

import org.example.project.dbFactory.DatabaseFactory
import org.example.project.di.initKoin
import org.koin.dsl.module

val iosModules = module {
    single { DatabaseFactory() }

}

fun initKoinIOS() = initKoin(additionalModule = listOf(iosModules))