package org.example.project.features.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.savedstate.read
import org.example.project.features.app.data.Screen
import org.example.project.features.detail.ui.DetailRoute

const val WORKOUT_ID_ARG = "exerciseId"

fun NavController.navigateToDetail(exerciseId: String, navOptions: NavOptions? = null) {
    navigate(
        Screen.Detail.route.replace(
            "$WORKOUT_ID_ARG={$WORKOUT_ID_ARG}", "$WORKOUT_ID_ARG=$exerciseId"
        )
    )
}

fun NavGraphBuilder.detailNavGraph(
    onBackClick: () -> Unit,
    isUserLoggedIn: () -> Boolean,
    openLoginBottomSheet: (() -> Unit) -> Unit,
) {
    composable(
        Screen.Detail.route,
        arguments = listOf(
            navArgument(WORKOUT_ID_ARG) {
                type = NavType.StringType
            }
        )
    ) {
        val exerciseId = it.arguments?.read { getString(WORKOUT_ID_ARG) } ?: ""
        DetailRoute(
            exerciseId = exerciseId,
            isUserLoggedIn = isUserLoggedIn,
            openLoginBottomSheet = openLoginBottomSheet,
            onBackClick = onBackClick
        )
    }
}