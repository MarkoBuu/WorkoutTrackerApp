package org.example.project.features.detail.data.datasource

import org.example.project.features.common.data.database.daos.FavoriteWorkoutDao
import org.example.project.features.common.data.database.daos.WorkoutTrackerDao
import org.example.project.features.common.domain.entities.WorkoutDetailItem

class WorkoutDetailLocalDataSourceImpl (
    private val workoutDao : WorkoutTrackerDao,
    private val favoriteWorkoutDao: FavoriteWorkoutDao
): WorkoutDetailLocalDataSource {

    override suspend fun getWorkoutDetail(exerciseId: String): WorkoutDetailItem? {
       return workoutDao.getWorkoutDetailById(exerciseId)
    }

    override suspend fun saveWorkout(workout: WorkoutDetailItem) {
        workoutDao.insertWorkoutDetail(workout)
    }

    override suspend fun addFavorite(exerciseId: String) {
        favoriteWorkoutDao.addFavorite(exerciseId)
    }

    override suspend fun removeFavorite(exerciseId: String) {
        favoriteWorkoutDao.removeFavorite(exerciseId)
    }

    override suspend fun isFavorite(exerciseId: String): Boolean {
        return favoriteWorkoutDao.isFavorite(exerciseId)
    }
}