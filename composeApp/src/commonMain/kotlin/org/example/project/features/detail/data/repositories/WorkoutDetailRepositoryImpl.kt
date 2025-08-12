package org.example.project.features.detail.data.repositories

import org.example.project.features.common.domain.entities.WorkoutDetailItem
import org.example.project.features.detail.data.datasource.WorkoutDetailLocalDataSource
import org.example.project.features.detail.data.datasource.WorkoutDetailRemoteDataSource

class WorkoutDetailRepositoryImpl(
    private val workoutDetailLocalDataSource: WorkoutDetailLocalDataSource,
    private val workoutDetailRemoteDataSource: WorkoutDetailRemoteDataSource
): WorkoutDetailRepository {

    override suspend fun getWorkoutDetailList(exerciseId : String): Result<WorkoutDetailItem>{
        return try {
            val workoutDetailCache = workoutDetailLocalDataSource.getWorkoutDetail(exerciseId)
            return if(workoutDetailCache != null){
                Result.success(workoutDetailCache)
            } else {
                val workoutDetailApiResponse = workoutDetailRemoteDataSource.getWorkoutDetail(exerciseId)
                    ?: return Result.failure(Exception("Workout not found"))
                workoutDetailLocalDataSource.saveWorkout(workoutDetailApiResponse)
                Result.success(workoutDetailApiResponse)
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}