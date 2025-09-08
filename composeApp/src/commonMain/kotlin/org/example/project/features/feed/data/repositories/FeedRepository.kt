package org.example.project.features.feed.data.repositories

import org.example.project.features.common.domain.entities.WorkoutItem

interface FeedRepository {
    suspend fun getWorkoutList() : Result<List<WorkoutItem>>
}

