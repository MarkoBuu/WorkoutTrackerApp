package org.example.project.features.detail.data.datasource

import org.example.project.features.common.domain.entities.WorkoutDetailItem

interface WorkoutDetailRemoteDataSource {
    suspend fun getWorkoutDetail(exerciseId: String): WorkoutDetailItem?
}