package org.example.project.features.feed.data.datasources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import org.example.project.features.common.data.api.BASE_URL
import org.example.project.features.common.data.models.WorkoutListApiResponse
import org.example.project.features.common.data.models.toWorkoutItem
import org.example.project.features.common.domain.entities.WorkoutItem

class FeedRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : FeedRemoteDataSource {

    override suspend fun getWorkoutsList(): List<WorkoutItem> {
        val httpResponse = httpClient.get("${BASE_URL}/exercises") {
            url {
                parameters.append("limit", "30")
            }
            header("x-rapidapi-key", "27718a3d09msh8b321aa8091d452p1493b2jsn1c52c8a15f6f")
        }
        val workoutListApiResponse = httpResponse.body<WorkoutListApiResponse>()
        return workoutListApiResponse.data.mapNotNull { it.toWorkoutItem() }
    }
}