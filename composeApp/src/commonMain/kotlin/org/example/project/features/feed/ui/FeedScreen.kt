package org.example.project.features.feed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.wojciechosak.calendar.animation.CalendarAnimator
import io.wojciechosak.calendar.utils.today
import io.wojciechosak.calendar.view.WeekView
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.example.project.features.components.ErrorContent
import org.example.project.features.components.Loader
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
    val workouts = feedUiState.workoutsList
    Scaffold(
        topBar = { AppBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when {
                feedUiState.isLoading -> {
                    Loader()
                }

                feedUiState.isError != null -> {
                    ErrorContent()
                }

                workouts != null -> {
                    Calendar()
                }
            }
        }
    }
}

@Composable
fun Calendar() {
    val startDate by remember { mutableStateOf(LocalDate.today()) }
    val calendarAnimator by remember { mutableStateOf(CalendarAnimator(startDate)) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        WeekView(
            startDate = startDate,
            calendarAnimator = calendarAnimator,
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    calendarAnimator.animateTo(LocalDate.today())
                }
            },
            modifier = Modifier
                .width(100.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(10.dp)),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                "Today",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp
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
                text = "Hi Marko!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
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