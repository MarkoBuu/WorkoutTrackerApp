package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.example.project.features.app.data.rememberAppState
import org.example.project.features.app.navigation.AppNavHost
import org.example.project.features.designSystem.theme.WorkoutTrackerAppCmpTheme
import org.example.project.features.login.auth.AuthServiceImpl
import org.example.project.features.login.ui.LoginScreenBottomSheet
import org.example.project.features.login.ui.LoginViewModel
import org.example.project.features.login.ui.SignUpBottomSheet
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App(

) {
    val loginViewModel = LoginViewModel(AuthServiceImpl(auth = Firebase.auth))

    WorkoutTrackerAppCmpTheme {
        val navController = rememberNavController()
        val appState = rememberAppState(
            navController, scope = rememberCoroutineScope(),
            appPreferences = koinInject()
        )

        var showLoginBottomSheet by remember {
            mutableStateOf(false)
        }

        val isLoggedIn by appState.isLoggedIn.collectAsState()

        val isUserLoggedIn : () -> Boolean = {
            isLoggedIn
        }

        var loginCallback: () -> Unit by remember {
            mutableStateOf({})
        }

        val openLoginBottomSheet: (() -> Unit) -> Unit = {
            showLoginBottomSheet = true
            loginCallback = it
        }

        val onLoginSuccess: () -> Unit = {
            showLoginBottomSheet = false
            appState.updateIsLoggedIn(true)
            loginCallback()
        }

        val onLogout: () -> Unit = {
            appState.onLogout()
        }

        val onCloseSheet: () -> Unit = {
            showLoginBottomSheet = false
        }

        var showSignUpBottomSheet by remember { mutableStateOf(false) }

        val openSignUpBottomSheet: (() -> Unit) -> Unit = { callback ->
            showSignUpBottomSheet = true
            loginCallback = callback
        }

        val onSignUpSuccess: () -> Unit = {
            showSignUpBottomSheet = false
            appState.updateIsLoggedIn(true)
            loginCallback()
        }

        LoginScreenBottomSheet(
            loginViewModel = loginViewModel,
            showBottomSheet = showLoginBottomSheet,
            onClose = onCloseSheet,
            onLoginSuccess = onLoginSuccess,
            onSignOut = loginViewModel::onSignOut,
        )

        SignUpBottomSheet(
            loginViewModel = loginViewModel,
            showBottomSheet = showSignUpBottomSheet,
            onClose = { showSignUpBottomSheet = false },
            onSignUpSuccess = onSignUpSuccess,
            onSignOut = loginViewModel::onSignOut
        )

        AppNavHost(
            appState = appState,
            isUserLoggedIn = isUserLoggedIn,
            openLoginBottomSheet = openLoginBottomSheet,
            onLogout = onLogout,
            openSignUpBottomSheet = openSignUpBottomSheet
        )
    }
}