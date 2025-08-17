package org.example.project.features.favorites.ui

import org.example.project.features.common.domain.entities.WorkoutDetailItem

data class FavoritesScreenUiState (
    val favoriteWorkoutsList: List<WorkoutDetailItem>? =  null,
    val isLoading: Boolean = true,
    val isError: String? = null
)