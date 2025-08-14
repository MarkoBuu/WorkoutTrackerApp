package org.example.project.features.common.data.models
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.example.project.features.common.domain.entities.WorkoutDetailItem

@Serializable
data class WorkoutDetailApiItem(
    @SerialName("exerciseId")
    val exerciseId: String ?= null,
    @SerialName("name")
    val name: String ?= null,
    @SerialName("imageUrl")
    val imageUrl: String ?= null,
    @SerialName("equipments")
    val equipments: List<String> ?= null,
    @SerialName("bodyParts")
    val bodyParts: List<String> ?= null,
    @SerialName("exerciseType")
    val exerciseType: String ?= null,
    @SerialName("targetMuscles")
    val targetMuscles: List<String> ?= null,
    @SerialName("secondaryMuscles")
    val secondaryMuscles: List<String> ?= null,
    @SerialName("videoUrl")
    val videoUrl: String ?= null,
    @SerialName("keywords")
    val keywords: List<String> ?= null,
    @SerialName("overview")
    val overview: String ?= null,
    @SerialName("instructions")
    val instructions: List<String> ?= null,
    @SerialName("exerciseTips")
    val exerciseTips: List<String> ?= null,
    @SerialName("variations")
    val variations: List<String> ?= null,
    @SerialName("relatedExerciseIds")
    val relatedExerciseIds: List<String> ?= null,
)

fun WorkoutDetailApiItem.toWorkoutDetailItem(): WorkoutDetailItem? {
    return if(exerciseId != null)
        WorkoutDetailItem(
            exerciseId = this.exerciseId,
            name = name ?: "",
            imageUrl = imageUrl ?: "",
            bodyParts = bodyParts ?: emptyList(),
            equipments = equipments ?: emptyList(),
            exerciseType = exerciseType ?: "",
            targetMuscles = targetMuscles ?: emptyList(),
            secondaryMuscles = secondaryMuscles ?: emptyList(),
            keywords = keywords ?: emptyList(),
            overview = overview ?: "",
            instructions =instructions?: emptyList(),
            exerciseTips = exerciseTips?: emptyList(),
            variations = variations?: emptyList(),
            relatedExerciseIds = relatedExerciseIds?: emptyList(),
            videoUrl = videoUrl?:"",
            isFavorite = false
        )
    else null
}

