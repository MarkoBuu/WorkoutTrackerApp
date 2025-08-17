package org.example.project.features.favorites.data

import org.example.project.features.common.data.database.daos.FavoriteWorkoutDao
import org.example.project.features.common.domain.entities.WorkoutDetailItem

class FavoriteWorkoutLocalDataSourceImpl(
    private val favoriteWorkoutDao: FavoriteWorkoutDao
): FavoriteWorkoutLocalDataSource {
    override suspend fun getAllFavoriteWorkouts(): List<WorkoutDetailItem>{
        return favoriteWorkoutDao.getAllFavorites()
    }
    override suspend fun addFavorite(exerciseId: String) {
        favoriteWorkoutDao.addFavorite(exerciseId)
    }
    override suspend fun removeFavorite(exerciseId: String){
        favoriteWorkoutDao.removeFavorite(exerciseId)
    }
}