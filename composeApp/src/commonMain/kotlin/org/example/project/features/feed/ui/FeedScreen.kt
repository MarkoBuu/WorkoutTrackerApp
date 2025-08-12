package org.example.project.features.feed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.wojciechosak.calendar.animation.CalendarAnimator
import io.wojciechosak.calendar.config.rememberCalendarState
import io.wojciechosak.calendar.utils.daySimpleName
import io.wojciechosak.calendar.utils.today
import io.wojciechosak.calendar.view.CalendarDay
import io.wojciechosak.calendar.view.CalendarView
import io.wojciechosak.calendar.view.HorizontalCalendarView
import io.wojciechosak.calendar.view.WeekView
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.example.project.features.common.domain.entities.WorkoutItem
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
        topBar = { TopAppBar() }
    ) { innerPadding ->
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

@Composable
fun Calendar() {
    val startDate by remember { mutableStateOf(LocalDate(2025, Month.AUGUST, 11)) }
    val calendarAnimator by remember { mutableStateOf(CalendarAnimator(startDate)) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        calendarAnimator.animateTo(LocalDate.today())
                    }
                },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(16.dp)),
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

        Spacer(modifier = Modifier.height(16.dp))

        WeekView(
            startDate = startDate,
            calendarAnimator = calendarAnimator,
        )
    }
}

@Composable
private fun TopAppBar(){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(text = "Hi Marko!", color = MaterialTheme.colorScheme.primaryContainer,
            style = MaterialTheme.typography.titleMedium)
    }
}