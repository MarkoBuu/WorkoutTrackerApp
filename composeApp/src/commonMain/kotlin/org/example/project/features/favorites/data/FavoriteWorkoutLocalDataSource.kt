package org.example.project.features.favorites.data

import org.example.project.features.common.domain.entities.WorkoutDetailItem

interface FavoriteWorkoutLocalDataSource {
    suspend fun getAllFavoriteWorkouts(): List<WorkoutDetailItem>
    suspend fun addFavorite(exerciseId: String)
    suspend fun removeFavorite(exerciseId: String)
}