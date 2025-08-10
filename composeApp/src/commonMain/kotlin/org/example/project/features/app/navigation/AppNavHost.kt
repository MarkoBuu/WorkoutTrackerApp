package org.example.project.features.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.example.project.features.app.data.AppState
import org.example.project.features.app.data.Screen
import org.example.project.features.detail.navigation.detailNavGraph
import org.example.project.features.search.navigation.searchNavGraph
import org.example.project.features.tabs.navigation.tabsNavGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    appState: AppState,
    startDestination: String = Screen.Tabs.route
) {
    val navController = appState.navController

    val tabNavController = rememberNavController()

    NavHost(
        navController, startDestination
    ) {
        tabsNavGraph(
            tabNavController = tabNavController
        )
        searchNavGraph()
        detailNavGraph()

    }
}