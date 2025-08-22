package org.example.project.features.workouts.data

import kotlinx.datetime.LocalDate
import org.example.project.features.common.domain.entities.WorkoutItem

data class WorkoutSession(
    val id: String,
    val date: LocalDate,
    val workoutItems: List<WorkoutItem>,
    val duration: Int? = null,
    val notes: String? = null
)