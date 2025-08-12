package org.example.project.features.common.domain.entities

data class WorkoutDetailItem(
    val exerciseId: String,
    val name: String,
    val imageUrl: String,
    val bodyParts: List<String>,
    val equipments: List<String>,
    val exerciseType: String,
    val targetMuscles: List<String>,
    val secondaryMuscles: List<String>,
    val keywords: List<String>,
    val overview: String,
    val instructions: List<String>,
    val exerciseTips: List<String>,
    val variations: List<String>,
    val relatedExerciseIds: List<String>,
    val videoUrl: String,
)