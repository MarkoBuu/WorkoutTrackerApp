package org.example.project.di

import org.example.project.dbFactory.DatabaseFactory
import org.example.project.di.initKoin
import org.koin.dsl.module

val jvmModules = module {
    single { DatabaseFactory() }
}

fun initKoinJvm() = initKoin(additionalModule = listOf(jvmModules))