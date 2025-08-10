package org.example.project

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.example.project.features.app.data.rememberAppState
import org.example.project.features.app.navigation.AppNavHost
import org.example.project.features.designSystem.theme.WorkoutTrackerAppCmpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    WorkoutTrackerAppCmpTheme {
        val navController = rememberNavController()
        val appState = rememberAppState(navController)
        AppNavHost(
            appState = appState
        )
    }
}