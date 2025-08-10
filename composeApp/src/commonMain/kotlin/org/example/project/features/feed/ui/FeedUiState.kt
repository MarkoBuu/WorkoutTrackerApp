package org.example.project.features.feed.ui

import org.example.project.features.common.domain.entities.WorkoutItem

data class FeedUiState (
    val workoutsList: List<WorkoutItem> ?=  null,
    val isLoading: Boolean = true,
    val isError: String? = null
)