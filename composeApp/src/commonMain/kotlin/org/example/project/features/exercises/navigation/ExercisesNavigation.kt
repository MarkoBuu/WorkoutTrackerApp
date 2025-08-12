package org.example.project.features.exercises.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.example.project.features.app.data.Screen
import org.example.project.features.exercises.ui.ExercisesRoute

fun NavController.navigateToExercises(navOptions: NavOptions? = null) {
    navigate(Screen.Exercises.route)
}

fun NavGraphBuilder.exercisesNavGraph(
) {
    composable(Screen.Exercises.route) {
        ExercisesRoute(

        )
    }
}