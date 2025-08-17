package org.example.project.features.favorites.domain

import org.example.project.features.common.domain.entities.WorkoutDetailItem
import org.example.project.features.favorites.data.FavoriteWorkoutLocalDataSource

class FavoriteWorkoutRepositoryImpl(
    private val favoriteWorkoutLocalDataSource: FavoriteWorkoutLocalDataSource
): FavoriteWorkoutRepository {
    override suspend fun getAllFavoriteWorkouts(): Result<List<WorkoutDetailItem>>{
        return try {
            val list = favoriteWorkoutLocalDataSource.getAllFavoriteWorkouts()
            return Result.success(list)

        }catch (e: Exception){
            Result.failure(e)
        }
    }
    override suspend fun addFavorite(exerciseId: String) {
        favoriteWorkoutLocalDataSource.addFavorite(exerciseId)
    }
    override suspend fun removeFavorite(exerciseId: String) {
        favoriteWorkoutLocalDataSource.removeFavorite(exerciseId)

    }
}