package org.example.project.features.detail.ui

import org.example.project.features.common.domain.entities.WorkoutDetailItem

data class WorkoutDetailUiState (
    val workoutDetail: WorkoutDetailItem? =  null,
    val isLoading: Boolean = true,
    val isError: String? = null
)