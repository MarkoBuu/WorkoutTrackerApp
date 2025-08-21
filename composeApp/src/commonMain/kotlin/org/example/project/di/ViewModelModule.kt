package org.example.project.di

import org.example.project.features.favorites.ui.FavoritesScreenViewModel
import org.example.project.features.detail.ui.WorkoutDetailViewModel
import org.example.project.features.feed.ui.FeedViewModel
import org.example.project.features.login.ui.LoginViewModel
import org.example.project.features.profile.ui.ProfileViewModel
import org.example.project.features.search.ui.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun viewModelModule() = module {
    viewModel{
        FeedViewModel(get())
    }

    viewModel{
        WorkoutDetailViewModel(get())
    }

    viewModel{
        FavoritesScreenViewModel(get())
    }
    viewModel{
        ProfileViewModel()
    }

    viewModel {
        LoginViewModel()
    }

    viewModel {
        SearchViewModel(get())
    }
}