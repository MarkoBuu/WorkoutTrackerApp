package org.example.project.features.feed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoute(
    navigateToSearch: () -> Unit,
    feedViewModel: FeedViewModel = koinViewModel()
) {
    val feedUiState = feedViewModel.feedUiState.collectAsState()

    FeedScreen(
        feedUiState = feedUiState.value,
        navigateToSearch = navigateToSearch
    )
}

@Composable
fun FeedScreen(
    feedUiState: FeedUiState,
    navigateToSearch: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("FeedScreen")
    }
}