package org.example.project.features.search.data.datasources

import org.example.project.features.common.data.database.daos.WorkoutTrackerDao
import org.example.project.features.common.domain.entities.WorkoutItem

class SearchWorkoutLocalDataSourceImpl(
    private val workoutTrackerDao: WorkoutTrackerDao
) : SearchWorkoutLocalDataSource{
   override suspend fun searchWorkoutByText(query: String): List<WorkoutItem>{
    return workoutTrackerDao.searchWorkoutByText(query)
   }
}