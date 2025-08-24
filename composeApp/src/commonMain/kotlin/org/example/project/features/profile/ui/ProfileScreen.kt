package org.example.project.features.profile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.features.components.ErrorContent
import org.example.project.features.components.Loader
import org.example.project.features.profile.data.User
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import workouttracker.composeapp.generated.resources.Res
import workouttracker.composeapp.generated.resources.avatar
import workouttracker.composeapp.generated.resources.profile_dummy

@Composable
fun ProfileRoute(
    profileViewModel: ProfileViewModel = koinViewModel(),
    isUserLoggedIn: () -> Boolean,
    openLoginBottomSheet: (() -> Unit) -> Unit,
    openSignUpBottomSheet: (() -> Unit) -> Unit,
    onLogout: () -> Unit
) {
    val profileUiState by profileViewModel.profileUiState.collectAsState()

    ProfileScreen(
        isUserLoggedIn = isUserLoggedIn,
        profileUiState = profileUiState,
        onEditProfile = {
        },
        onSignUp = {
            openSignUpBottomSheet {
                profileViewModel.refresh()
            }
        },
        onLogin = {
            openLoginBottomSheet {
                profileViewModel.refresh()
            }
        },
        onLogout = {
            profileViewModel.signOut()
            onLogout()
        },
        profileViewModel = profileViewModel
    )
}

@Composable
fun ProfileScreen(
    isUserLoggedIn: () -> Boolean,
    profileUiState: ProfileScreenUiState,
    onEditProfile: () -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel
) {

    Scaffold(
        topBar = { AppBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            HorizontalDivider(
                thickness = 0.3.dp,
                color = MaterialTheme.colorScheme.outline.copy(
                    alpha = 0.5f
                )
            )

            when {

                !isUserLoggedIn() -> {
                    NotLoggedInProfileScreen(
                        onLogin = onLogin,
                        onSignUp = onSignUp
                    )
                }

                profileUiState.isLoading -> {
                    Loader()
                }

                profileUiState.error != null -> {
                    ErrorContent({
                        profileViewModel.getUserInfo(user = profileUiState.userInfo)
                    })
                }

                profileUiState.userInfo != null && isUserLoggedIn() -> {
                    ProfileContent(
                        userInfo = profileUiState.userInfo,
                        onEditProfile = onEditProfile,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    userInfo: User,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(Res.drawable.avatar),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    0.3.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), CircleShape
                ).background(MaterialTheme.colorScheme.outline),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (userInfo.email.isNotBlank()) {
            Text(
                userInfo.email,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            "ID: ${userInfo.id.take(8)}...",
            style = TextStyle(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Log Out", style = TextStyle(fontSize = 16.sp))
        }
    }
}

@Composable
fun NotLoggedInProfileScreen(
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(Res.drawable.profile_dummy),
            contentDescription = null,
            modifier = Modifier.size(120.dp).clip(CircleShape).border(
                0.3.dp, MaterialTheme.colorScheme.outline.copy(
                    alpha = 0.5f
                ), CircleShape
            ).background(MaterialTheme.colorScheme.outline), contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "You are not logged in",
            style = TextStyle(
                fontSize = 24.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Login to view your profile",
            style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                "Log In",
                style = TextStyle(
                    fontSize = 16.sp,
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            onClick = onSignUp,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                "Sign Up",
                style = TextStyle(
                    fontSize = 16.sp,
                )
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar() {
    TopAppBar(
        title = {
            Text(
                "Profile",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        windowInsets = WindowInsets(
            top = 0,
            bottom = 0
        ),
    )
}