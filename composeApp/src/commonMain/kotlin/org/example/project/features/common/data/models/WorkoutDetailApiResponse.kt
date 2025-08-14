package org.example.project.features.common.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDetailApiResponse (
    @SerialName("data")
    val data: WorkoutDetailApiItem,
    @SerialName("success")
    val success: Boolean = false
)
