package org.example.project.features.tabs.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.example.project.features.app.data.Screen
import org.example.project.features.tabs.ui.TabsRoute

fun NavController.navigateToTabs(navOptions: NavOptions? = null) {
    navigate(Screen.Tabs.route)
}

fun NavGraphBuilder.tabsNavGraph(
    tabNavController: NavHostController
) {
    composable(Screen.Tabs.route) {
        TabsRoute(
            tabNavController = tabNavController
        )
    }
}