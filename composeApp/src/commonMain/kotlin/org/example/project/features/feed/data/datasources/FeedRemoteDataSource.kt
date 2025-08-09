package org.example.project.features.feed.data.datasources

import org.example.project.features.common.domain.entities.WorkoutItem

interface FeedRemoteDataSource {
    suspend fun getWorkoutsList(): List<WorkoutItem>
}