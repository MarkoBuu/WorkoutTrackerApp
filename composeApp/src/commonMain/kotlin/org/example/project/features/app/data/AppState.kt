package org.example.project.features.app.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import org.example.project.features.tabs.navigation.navigateToTabs

@Composable
fun rememberAppState(
    navController: NavHostController,
    scope: CoroutineScope = rememberCoroutineScope()
): AppState{
    return remember(
        navController,
        scope
        ) {
        AppState(
            navController = navController,
            scope = scope
        )
    }
}

class AppState(
    val navController: NavHostController,
    scope : CoroutineScope
) {
    fun navigateToTabs() = navController.navigateToTabs()
}