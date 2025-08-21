package org.example.project.features.search.data.datasources

import org.example.project.features.common.domain.entities.WorkoutItem

interface SearchWorkoutLocalDataSource {
    suspend fun searchWorkoutByText(query: String): List<WorkoutItem>
}