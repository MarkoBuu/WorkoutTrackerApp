package org.example.project.features.favorites.domain

import org.example.project.features.common.domain.entities.WorkoutDetailItem

interface FavoriteWorkoutRepository {
    suspend fun getAllFavoriteWorkouts(): Result<List<WorkoutDetailItem>>
    suspend fun addFavorite(exerciseId: String)
    suspend fun removeFavorite(exerciseId: String)
}