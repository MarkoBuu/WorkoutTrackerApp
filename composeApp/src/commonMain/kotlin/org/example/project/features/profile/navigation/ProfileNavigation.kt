package org.example.project.features.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.example.project.features.app.data.Screen
import org.example.project.features.profile.ui.ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(Screen.Profile.route)
}

fun NavGraphBuilder.profileNavGraph(
) {
    composable(Screen.Profile.route) {
        ProfileRoute(

        )
    }
}