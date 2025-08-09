package org.example.project.features.common.data.models
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.example.project.features.common.domain.entities.WorkoutItem

@Serializable
data class WorkoutApiItem(
    @SerialName("exerciseId")
    val exerciseId: String ?= null,

    @SerialName("name")
    val name: String ?= null,

    @SerialName("imageUrl")
    val imageUrl: String ?= null,

    @SerialName("bodyParts")
    val bodyParts: List<String> ?= null,

    @SerialName("equipments")
    val equipments: List<String> ?= null,

    @SerialName("exerciseType")
    val exerciseType: String ?= null,

    @SerialName("targetMuscles")
    val targetMuscles: List<String> ?= null,

    @SerialName("secondaryMuscles")
    val secondaryMuscles: List<String> ?= null,

    @SerialName("keywords")
    val keywords: List<String> ?= null
)

fun WorkoutApiItem.toWorkoutItem(): WorkoutItem? {
    return if(exerciseId != null)
        WorkoutItem(
            exerciseId = exerciseId.toLong(),
            name = name ?: "",
            imageUrl = imageUrl ?: "",
            bodyParts = bodyParts ?: emptyList(),
            equipments = equipments ?: emptyList(),
            exerciseType = exerciseType ?: "",
            targetMuscles = targetMuscles ?: emptyList(),
            secondaryMuscles = secondaryMuscles ?: emptyList(),
            keywords = keywords ?: emptyList(),
            isFavorite = false

        ) else null
}

fun String.capitalizeFirstWord() = this.replaceFirstChar { it.uppercase() }
