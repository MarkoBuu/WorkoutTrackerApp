package org.example.project.features.search.data.datasources

import org.example.project.features.common.domain.entities.WorkoutItem
import org.example.project.features.search.domain.repo.SearchWorkoutRepository

class SearchWorkoutRepositoryImpl(
    private val searchWorkoutLocalDataSource: SearchWorkoutLocalDataSource
): SearchWorkoutRepository {
   override suspend fun searchWorkoutByText(query: String): Result<List<WorkoutItem>>{
       return try {
           val resultList = searchWorkoutLocalDataSource.searchWorkoutByText(query)
            Result.success(resultList)
       } catch (e: Exception){
           Result.failure(e)
       }
   }
}