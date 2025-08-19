package org.example.project.features.profile.data

data class User (
    val id: Long,
    val name: String,
    val email: String,
    val myWorkoutsCount: Int,
    val favoriteWorkoutCount: Int,
    val followers: Int
)