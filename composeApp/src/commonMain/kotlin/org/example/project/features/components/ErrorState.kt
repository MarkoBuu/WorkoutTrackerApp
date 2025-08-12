package org.example.project.features.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.features.feed.ui.FeedViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ErrorContent(
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Error while loading workouts",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.error
            )
        )

        Button(
            onClick = {}
        ){
            Text("Retry")
        }
    }
}