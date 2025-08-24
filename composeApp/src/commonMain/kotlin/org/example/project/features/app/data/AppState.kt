package org.example.project.features.app.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.features.app.data.AppConstants.IS_LOGGED_IN
import org.example.project.features.detail.navigation.navigateToDetail
import org.example.project.features.search.navigation.navigateToSearch
import org.example.project.features.tabs.navigation.navigateToTabs
import org.example.project.features.workouts.navigation.navigateToWorkout
import org.example.project.preferences.AppPreferences

@Composable
fun rememberAppState(
    navController: NavHostController,
    scope: CoroutineScope = rememberCoroutineScope(),
    appPreferences: AppPreferences
): AppState {
    return remember(
        navController,
        scope
    ) {
        AppState(
            navController = navController,
            scope = scope,
            appPreferences = appPreferences
        )
    }
}

class AppState(
    val navController: NavHostController,
    scope: CoroutineScope,
    private val appPreferences: AppPreferences
) {

    private var _isLoggedIn = MutableStateFlow(appPreferences.getBoolean(IS_LOGGED_IN, false))
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun navigateToTabs() = navController.navigateToTabs()
    fun navigateToDetail(exerciseId: String) = navController.navigateToDetail(exerciseId)
    fun navigateToSearch() = navController.navigateToSearch()
    fun navigateToWorkout() = navController.navigateToWorkout()
    fun navigateBack() = navController.navigateUp()

    fun updateIsLoggedIn(isLoggedIn: Boolean) {
        this._isLoggedIn.value = isLoggedIn
        appPreferences.putBoolean(IS_LOGGED_IN, isLoggedIn)
    }

    fun onLogout() {
        updateIsLoggedIn(false)
    }
}