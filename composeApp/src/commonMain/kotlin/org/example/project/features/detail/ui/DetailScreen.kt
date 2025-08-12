package org.example.project.features.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.project.features.components.ErrorContent
import org.example.project.features.components.Loader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailRoute(
    exerciseId: String,
    onBackClick: () -> Unit,
    detailViewModel: WorkoutDetailViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        detailViewModel.getWorkoutDetail(exerciseId)
    }

    val detailUiState = detailViewModel.detailUiState.collectAsState()
    DetailScreen(
        uiState = detailUiState.value, onBackClick = onBackClick
    )
}

@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    uiState: WorkoutDetailUiState
) {
    Scaffold(
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.isLoading -> {
                    Loader()
                }

                uiState.isError != null -> {
                    ErrorContent()
                }

                uiState.workoutDetail != null -> {
                    WorkoutDetailContent()
                }
            }
        }
    }
}

@Composable
fun WorkoutDetailContent() {
    Text("a")
}
