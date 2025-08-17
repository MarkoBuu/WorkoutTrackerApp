package org.example.project.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.example.project.features.common.data.database.DbHelper
import org.example.project.features.common.data.database.daos.FavoriteWorkoutDao
import org.example.project.features.common.data.database.daos.WorkoutTrackerDao
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

fun cacheModule() = module {
    single<CoroutineContext> { Dispatchers.Default }
    single { CoroutineScope(get()) }
    single { DbHelper(get()) }
    single { WorkoutTrackerDao(get()) }
    single { FavoriteWorkoutDao(get()) }
}