package org.example.project.features.common.data.database

import org.example.project.Workout
import org.example.project.features.common.domain.entities.WorkoutItem

fun workoutEntityMapper(workout: Workout) = WorkoutItem(
    exerciseId = workout.exerciseId,
    name = workout.name,
    imageUrl = workout.imageURL,
    bodyParts = workout.bodyParts,
    equipments = workout.equipments,
    exerciseType = workout.exerciseType,
    targetMuscles = workout.targetMuscles,
    secondaryMuscles = workout.secondaryMuscles,
    keywords = workout.keywords,
    isFavorite = workout.isFavorite == 1L
)