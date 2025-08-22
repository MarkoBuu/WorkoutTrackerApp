package org.example.project.features.feed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.wojciechosak.calendar.animation.CalendarAnimator
import io.wojciechosak.calendar.utils.today
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.example.project.features.common.domain.entities.WorkoutItem
import org.example.project.features.components.ErrorContent
import org.example.project.features.components.Loader
import org.example.project.features.workouts.data.WorkoutSession
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoute(
    navigateToWorkout: () -> Unit,
    feedViewModel: FeedViewModel = koinViewModel()
) {
    val feedUiState = feedViewModel.feedUiState.collectAsState()

    FeedScreen(
        feedUiState = feedUiState.value,
        navigateToWorkout = navigateToWorkout,
        feedViewModel = feedViewModel
    )
}

@Composable
fun FeedScreen(
    feedUiState: FeedUiState,
    navigateToWorkout: () -> Unit,
    feedViewModel: FeedViewModel
) {
    val selectedDate by feedViewModel.selectedDate.collectAsState()
    val filteredWorkouts by feedViewModel.filteredWorkouts.collectAsState()
    val filteredWorkoutItems by feedViewModel.filteredWorkoutItems.collectAsState()

    Scaffold(
        topBar = { AppBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddWorkoutOptions(feedViewModel, selectedDate)
                    /*navigateToWorkout()*/
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Workout")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            when {
                feedUiState.isLoading -> Loader()
                feedUiState.isError != null -> ErrorContent { feedViewModel.getWorkoutList() }
                else -> {
                    Calendar(feedViewModel = feedViewModel)

                    Spacer(modifier = Modifier.height(16.dp))

                    WorkoutsForDate(
                        workoutItems = filteredWorkoutItems,
                        workoutSessions = filteredWorkouts,
                        selectedDate = selectedDate
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutsForDate(
    workoutItems: List<WorkoutItem>,
    workoutSessions: List<WorkoutSession>,
    selectedDate: LocalDate
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Workouts on ${selectedDate.dayOfMonth}.${selectedDate.monthNumber}.${selectedDate.year}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (workoutItems.isEmpty() && workoutSessions.isEmpty()) {
            Text(
                "No workouts recorded for this date",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        } else {
            // Show individual workout items
            if (workoutItems.isNotEmpty()) {
                Text(
                    "Exercises:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyColumn {
                    items(workoutItems) { workoutItem ->
                        WorkoutItemCard(workoutItem = workoutItem)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // Show workout sessions
            if (workoutSessions.isNotEmpty()) {
                Text(
                    "Workout Sessions:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyColumn {
                    items(workoutSessions) { session ->
                        WorkoutSessionCard(session = session)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutItemCard(workoutItem: WorkoutItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                workoutItem.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Target: ${workoutItem.targetMuscles.joinToString()}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Equipment: ${workoutItem.equipments.joinToString()}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun WorkoutSessionCard(session: WorkoutSession) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Workout Session",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${session.workoutItems.size} exercises",
                style = MaterialTheme.typography.bodySmall
            )
            session.duration?.let { duration ->
                Text("Duration: $duration minutes", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// Simple function to simulate adding a workout
private fun showAddWorkoutOptions(feedViewModel: FeedViewModel, date: LocalDate) {
    // This would typically open a dialog or navigate to exercise selection
    // For demo purposes, let's add a sample workout item
    val sampleWorkoutItem = WorkoutItem(
        exerciseId = "sample_${Clock.System.now().epochSeconds}",
        name = "Sample Exercise",
        imageUrl = "",
        bodyParts = listOf("Chest"),
        equipments = listOf("Dumbbell"),
        exerciseType = "Strength",
        targetMuscles = listOf("Pectorals"),
        secondaryMuscles = listOf("Triceps"),
        keywords = listOf("chest", "dumbbell"),
        isFavorite = false
    )

    feedViewModel.addWorkoutItem(date, sampleWorkoutItem)
}

@Composable
fun Calendar(feedViewModel: FeedViewModel) {
    SimpleWeekView(
        selectedDate = feedViewModel.selectedDate.collectAsState().value,
        onDateSelected = { feedViewModel.selectDate(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar() {
    TopAppBar(
        title = {
            Text(
                "WorkoutTracker",
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

@Composable
fun DateChip(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                date.dayOfMonth.toString(),
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                date.dayOfWeek.name.take(3),
                color = contentColor,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun SimpleWeekView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentDate = LocalDate.today()
    val weekDates = remember(selectedDate) {
        val startOfWeek = selectedDate.minus(DatePeriod(days = selectedDate.dayOfWeek.ordinal))
        List(7) { index -> startOfWeek.plus(DatePeriod(days = index)) }
    }
    val calendarAnimator by remember { mutableStateOf(CalendarAnimator(currentDate)) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekDates.forEach { date ->
                DateChip(
                    date = date,
                    isSelected = date == selectedDate,
                    onClick = { onDateSelected(date) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                onClick = { onDateSelected(selectedDate.minus(DatePeriod(days = 7))) },
                modifier = Modifier.size(48.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous week",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            FloatingActionButton(
                onClick = {
                    val today = LocalDate.today()
                    onDateSelected(today)
                    coroutineScope.launch {
                        calendarAnimator.animateTo(today)
                    }
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(48.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Today",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            FloatingActionButton(
                onClick = { onDateSelected(selectedDate.plus(DatePeriod(days = 7))) },
                modifier = Modifier.size(48.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next week",
                )
            }
        }
    }
}