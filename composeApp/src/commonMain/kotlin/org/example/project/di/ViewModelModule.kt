package org.example.project.di

import org.example.project.features.feed.ui.FeedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun viewModelModule() = module {
    viewModel{
        FeedViewModel(get())
    }
}