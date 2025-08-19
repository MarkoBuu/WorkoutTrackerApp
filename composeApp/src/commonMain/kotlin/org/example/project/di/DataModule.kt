package org.example.project.di

import org.example.project.features.detail.data.datasource.WorkoutDetailLocalDataSource
import org.example.project.features.detail.data.datasource.WorkoutDetailLocalDataSourceImpl
import org.example.project.features.detail.data.datasource.WorkoutDetailRemoteDataSource
import org.example.project.features.detail.data.datasource.WorkoutDetailRemoteDataSourceImpl
import org.example.project.features.detail.data.repositories.WorkoutDetailRepository
import org.example.project.features.detail.data.repositories.WorkoutDetailRepositoryImpl
import org.example.project.features.favorites.data.FavoriteWorkoutLocalDataSource
import org.example.project.features.favorites.data.FavoriteWorkoutLocalDataSourceImpl
import org.example.project.features.favorites.domain.FavoriteWorkoutRepository
import org.example.project.features.favorites.domain.FavoriteWorkoutRepositoryImpl
import org.example.project.features.feed.data.datasources.FeedLocalDataSource
import org.example.project.features.feed.data.datasources.FeedLocalDataSourceImpl
import org.example.project.features.feed.data.datasources.FeedRemoteDataSource
import org.example.project.features.feed.data.datasources.FeedRemoteDataSourceImpl
import org.example.project.features.feed.data.repositories.FeedRepositoryImpl
import org.example.project.features.feed.domain.repositories.FeedRepository
import org.example.project.preferences.AppPreferences
import org.example.project.preferences.AppPreferencesImpl
import org.koin.dsl.module

fun dataModule() = module {

    single<AppPreferences> { AppPreferencesImpl(get()) }
    single<FeedLocalDataSource> { FeedLocalDataSourceImpl(get()) }
    single<FeedRemoteDataSource> { FeedRemoteDataSourceImpl(get()) }


    single<WorkoutDetailLocalDataSource> { WorkoutDetailLocalDataSourceImpl(get(), get()) }
    single<WorkoutDetailRemoteDataSource> { WorkoutDetailRemoteDataSourceImpl(get()) }

    single<FavoriteWorkoutLocalDataSource> {  FavoriteWorkoutLocalDataSourceImpl(get())}

    single<FeedRepository> { FeedRepositoryImpl(get(), get()) }
    single<WorkoutDetailRepository> { WorkoutDetailRepositoryImpl(get(), get()) }
    single<FavoriteWorkoutRepository> { FavoriteWorkoutRepositoryImpl(get()) }

}