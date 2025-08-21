package org.example.project.features.search.domain.repo

import org.example.project.features.common.domain.entities.WorkoutDetailItem
import org.example.project.features.common.domain.entities.WorkoutItem

interface SearchWorkoutRepository {
    suspend fun searchWorkoutByText(query: String): Result<List<WorkoutItem>>
}