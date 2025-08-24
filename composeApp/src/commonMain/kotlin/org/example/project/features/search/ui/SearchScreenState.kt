package org.example.project.features.search.ui

import org.example.project.features.common.domain.entities.WorkoutItem

data class SearchScreenState (
    val idle: Boolean = true,
    val results: List<WorkoutItem> =  emptyList(),
    val success: Boolean = false,
    val isError: String? = null
)