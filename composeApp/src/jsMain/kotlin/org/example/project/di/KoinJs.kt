package org.example.project.di

import org.example.project.dbFactory.DatabaseFactory
import org.example.project.di.initKoin
import org.koin.dsl.module

val jsModules = module {
    single { DatabaseFactory() }
}

fun initKoinJs() = initKoin(additionalModule = listOf(jsModules))