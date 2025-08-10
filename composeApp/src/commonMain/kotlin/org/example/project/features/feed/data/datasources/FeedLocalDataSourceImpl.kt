package org.example.project.features.feed.data.datasources

import org.example.project.features.common.data.database.daos.WorkoutTrackerDao
import org.example.project.features.common.domain.entities.WorkoutItem

class FeedLocalDataSourceImpl(
    private val workoutDao: WorkoutTrackerDao
) : FeedLocalDataSource{
    override suspend fun getWorkoutsList(): List<WorkoutItem> {
        return workoutDao.getAllWorkouts()
    }
    override suspend fun saveWorkoutsList(workouts: List<WorkoutItem>) {
        workoutDao.insertWorkoutsBulk(workouts)
    }
}