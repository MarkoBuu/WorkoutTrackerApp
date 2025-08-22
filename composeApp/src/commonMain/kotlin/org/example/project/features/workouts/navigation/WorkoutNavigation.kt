package org.example.project.features.workouts.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.example.project.features.app.data.Screen
import org.example.project.features.workouts.ui.WorkoutRoute

fun NavController.navigateToWorkout(navOptions: NavOptions? = null) {
    navigate(Screen.Workouts.route)
}

fun NavGraphBuilder.workoutNavGraph(
) {
    composable(Screen.Workouts.route) {
        WorkoutRoute()
    }
}