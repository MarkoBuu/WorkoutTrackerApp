package org.example.project.features.common.domain.entities

data class WorkoutItem(
    val exerciseId: Long,          // "exr_41n2hHLE8aJXaxKR"
    val name: String,                // "Cobra Push-up"
    val imageUrl: String,            // URL to image
    val bodyParts: List<String>,     // ["CHEST", "HIPS"]
    val equipments: List<String>,    // ["BODY WEIGHT"]
    val exerciseType: String,        // "STRENGTH"
    val targetMuscles: List<String>,
    val secondaryMuscles: List<String>,
    val keywords: List<String>,
    val isFavorite: Boolean
)