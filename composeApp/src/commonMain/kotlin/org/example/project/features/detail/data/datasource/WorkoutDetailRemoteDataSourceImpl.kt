package org.example.project.features.detail.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import org.example.project.features.common.data.api.BASE_URL
import org.example.project.features.common.data.models.WorkoutDetailApiResponse
import org.example.project.features.common.data.models.toWorkoutDetailItem
import org.example.project.features.common.domain.entities.WorkoutDetailItem

class WorkoutDetailRemoteDataSourceImpl(
    private val httpClient: HttpClient
): WorkoutDetailRemoteDataSource {

    override suspend fun getWorkoutDetail(exerciseId: String): WorkoutDetailItem?{
        val httpResponse = httpClient.get("${BASE_URL}/exercises/$exerciseId") {
            header("x-rapidapi-key", "27718a3d09msh8b321aa8091d452p1493b2jsn1c52c8a15f6f")
        }
        return  httpResponse.body<WorkoutDetailApiResponse>().data.toWorkoutDetailItem()
    }
}