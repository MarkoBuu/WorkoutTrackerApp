package org.example.project.features.detail.data.datasource

import org.example.project.features.common.data.database.daos.WorkoutTrackerDao
import org.example.project.features.common.domain.entities.WorkoutDetailItem

class WorkoutDetailLocalDataSourceImpl (
    private val workoutDao : WorkoutTrackerDao
): WorkoutDetailLocalDataSource {

    override suspend fun getWorkoutDetail(exerciseId: String): WorkoutDetailItem? {
       return workoutDao.getWorkoutDetailById(exerciseId)
    }

    override suspend fun saveWorkout(workout: WorkoutDetailItem) {
        workoutDao.insertWorkoutDetail(workout)
    }
}