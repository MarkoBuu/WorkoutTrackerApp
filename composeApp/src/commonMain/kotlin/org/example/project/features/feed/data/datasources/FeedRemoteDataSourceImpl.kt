package org.example.project.features.feed.data.datasources

import org.example.project.features.common.domain.entities.WorkoutItem

class FeedRemoteDataSourceImpl : FeedRemoteDataSource {
    override suspend fun getWorkoutsList(): List<WorkoutItem> {
        TODO("Not yet implemented")
    }
}