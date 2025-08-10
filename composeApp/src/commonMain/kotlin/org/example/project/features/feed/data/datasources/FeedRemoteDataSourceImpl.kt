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
        val httpResponse = httpClient.get("$BASE_URL/exercises") {
            header("X-RapidAPI-Key", "75bb857f61msh9ac96c92e5a3a2ap10cea5jsn3510e9295bd8")
            header("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            url {
                parameters.append("limit", "10") // Default limit
            }
        }

        return httpResponse.body<WorkoutListApiResponse>().data
            .mapNotNull { it.toWorkoutItem() }
    }
}