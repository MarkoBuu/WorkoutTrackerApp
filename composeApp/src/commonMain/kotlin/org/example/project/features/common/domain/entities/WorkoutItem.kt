package org.example.project.features.common.domain.entities

data class WorkoutItem(
    val exerciseId: String,
    val name: String,
    val imageUrl: String,
    val bodyParts: List<String>,
    val equipments: List<String>,
    val exerciseType: String,
    val targetMuscles: List<String>,
    val secondaryMuscles: List<String>,
    val keywords: List<String>,
    val isFavorite: Boolean
)