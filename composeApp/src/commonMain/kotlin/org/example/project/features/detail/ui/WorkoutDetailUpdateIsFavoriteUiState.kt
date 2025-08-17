package org.example.project.features.detail.ui

import org.example.project.features.common.domain.entities.WorkoutDetailItem

data class WorkoutDetailUpdateIsFavoriteUiState (
    val isSuccess: Boolean? =  null,
    val isUpdating: Boolean = true,
    val isError: String? = null
)