package org.example.project.features.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.example.project.features.app.data.Screen
import org.example.project.features.detail.ui.DetailRoute

fun NavController.navigateToDetail(navOptions: NavOptions? = null) {
    navigate(Screen.Detail.route)
}

fun NavGraphBuilder.detailNavGraph(
) {
    composable(Screen.Detail.route) {
        DetailRoute(

        )
    }
}