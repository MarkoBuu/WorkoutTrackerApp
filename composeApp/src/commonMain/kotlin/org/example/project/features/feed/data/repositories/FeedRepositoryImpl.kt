package org.example.project.features.feed.data.repositories

import org.example.project.features.common.domain.entities.WorkoutItem
import org.example.project.features.feed.data.datasources.FeedLocalDataSource
import org.example.project.features.feed.data.datasources.FeedRemoteDataSource

class FeedRepositoryImpl(
    private val feedLocalDataSource: FeedLocalDataSource,
    private val feedRemoteDataSource: FeedRemoteDataSource

): FeedRepository {

    override suspend fun getWorkoutList() : Result<List<WorkoutItem>>{
        return try {
            val workoutListCache = feedLocalDataSource.getWorkoutsList()
            val count = workoutListCache.count()
            return if(count > 0){
                Result.success(workoutListCache)
            } else {
                val workoutListApiResponse = feedRemoteDataSource.getWorkoutsList()
                feedLocalDataSource.saveWorkoutsList(workoutListApiResponse)
                Result.success(workoutListApiResponse)
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}

