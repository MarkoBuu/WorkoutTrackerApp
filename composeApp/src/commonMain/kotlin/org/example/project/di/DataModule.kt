package org.example.project.di

import org.example.project.features.feed.data.datasources.FeedLocalDataSource
import org.example.project.features.feed.data.datasources.FeedLocalDataSourceImpl
import org.example.project.features.feed.data.datasources.FeedRemoteDataSource
import org.example.project.features.feed.data.datasources.FeedRemoteDataSourceImpl
import org.example.project.features.feed.data.repositories.FeedRepositoryImpl
import org.example.project.features.feed.domain.repositories.FeedRepository
import org.koin.dsl.module

fun dataModule() = module {
    single<FeedLocalDataSource> { FeedLocalDataSourceImpl(get()) }
    single<FeedRemoteDataSource> { FeedRemoteDataSourceImpl(get()) }
    single<FeedRepository> { FeedRepositoryImpl(get(), get()) }
}