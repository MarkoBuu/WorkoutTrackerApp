package org.example.project.features.detail.data.repositories

import org.example.project.features.common.domain.entities.WorkoutDetailItem

interface WorkoutDetailRepository {

    suspend fun getWorkoutDetailList(exerciseId: String): Result<WorkoutDetailItem>
    suspend fun addFavorite(exerciseId: String)
    suspend fun removeFavorite(exerciseId: String)
}