package org.example.project.features.common.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutListApiResponse (
    @SerialName("data")
    val data: List<WorkoutApiItem>,  // Default empty list for safety

    @SerialName("success")
    val success: Boolean = false,  // If API includes success flag

    @SerialName("meta")
    val meta: MetaData? = null  // For pagination if available
) {
    @Serializable
    data class MetaData(
        @SerialName("total") val total: Int = 0,
        @SerialName("hasNextPage") val hasNextPage: Boolean = false,
        @SerialName("hasPreviousPage") val hasPreviousPage: Boolean = false,
        @SerialName("nextCursor") val nextCursor: String? = null
    )
}
