package org.example.project.features.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.example.project.features.app.data.Screen
import org.example.project.features.search.ui.SearchRoute

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    navigate(Screen.Search.route)
}

fun NavGraphBuilder.searchNavGraph(
    navigateToDetail: (String) -> Unit,
    onBackPress: () -> Unit
) {
    composable(Screen.Search.route) {
        SearchRoute(
            navigateToDetail = navigateToDetail,
            onBackPress = onBackPress
        )
    }
}