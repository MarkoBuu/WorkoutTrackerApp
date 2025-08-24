package org.example.project.features.feed.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import org.example.project.features.profile.ui.ProfileViewModel
import org.example.project.features.workouts.data.WorkoutSession
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoute(
    navigateToWorkout: () -> Unit,
    isUserLoggedIn: () -> Boolean,
    openLoginBottomSheet: (() -> Unit) -> Unit,
    feedViewModel: FeedViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val feedUiState = feedViewModel.feedUiState.collectAsState()
    FeedScreen(
        feedUiState = feedUiState.value,
        navigateToWorkout = navigateToWorkout,
        feedViewModel = feedViewModel,
        profileViewModel = profileViewModel,
        isUserLoggedIn = isUserLoggedIn,
        openLoginBottomSheet = openLoginBottomSheet
    )
}

@Composable
fun FeedScreen(
    feedUiState: FeedUiState,
    navigateToWorkout: () -> Unit,
    feedViewModel: FeedViewModel,
    profileViewModel: ProfileViewModel,
    isUserLoggedIn: () -> Boolean,
    openLoginBottomSheet: (() -> Unit) -> Unit,
) {
    val selectedDate by feedViewModel.selectedDate.collectAsState()
    val filteredWorkouts by feedViewModel.filteredWorkouts.collectAsState()
    val filteredWorkoutItems by feedViewModel.filteredWorkoutItems.collectAsState()

    var showAlertDialog by remember { mutableStateOf(false) }

    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddWorkoutDialog(
            selectedDate = selectedDate,
            onDismiss = { showAddDialog = false },
            onSaveWorkout = { workoutItem ->
                feedViewModel.addWorkoutItem(selectedDate, workoutItem)
                showAddDialog = false
            }
        )
    }

    if (showAlertDialog) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = {
                showAlertDialog = false
            },
            title = {
                Text(
                    text = "Update Workouts",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            },
            text = {
                Text(
                    text = "Login to Add/Remove Workouts",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        showAlertDialog = false
                        openLoginBottomSheet {
                            profileViewModel.refresh()
                        }
                    }
                ) {
                    Text("Log In")
                }
            },
            dismissButton = {
                OutlinedButton(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.background

                    ),
                    onClick = {
                        showAlertDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = { AppBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!isUserLoggedIn()) {
                        showAlertDialog = true
                    } else {
                        showAddDialog = true
                    }
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
                        selectedDate = selectedDate,
                        feedViewModel = feedViewModel
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
    selectedDate: LocalDate,
    feedViewModel: FeedViewModel,

    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Workouts on: ${selectedDate.dayOfMonth}.${selectedDate.monthNumber}.${selectedDate.year}",
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
            if (workoutItems.isNotEmpty()) {
                LazyColumn {
                    items(workoutItems) { workoutItem ->
                        WorkoutItemCard(
                            workoutItem = workoutItem,
                            onDeleteClick = {
                                feedViewModel.deleteWorkoutItem(
                                    selectedDate,
                                    workoutItem
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

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
fun WorkoutItemCard(
    workoutItem: WorkoutItem,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Exercise?") },
            text = { Text("Are you sure you want to delete \"${workoutItem.name}\"? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick()
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    workoutItem.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Target muscles: ${workoutItem.targetMuscles.joinToString()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Workout type: ${workoutItem.exerciseType}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Equipment used: ${workoutItem.equipments.joinToString()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(
                onClick = { showDeleteConfirm = true },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete exercise",
                    tint = MaterialTheme.colorScheme.error
                )
            }
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

@Composable
fun AddWorkoutDialog(
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onSaveWorkout: (WorkoutItem) -> Unit
) {
    var exerciseName by remember { mutableStateOf("") }
    var equipment by remember { mutableStateOf("") }
    var exerciseType by remember { mutableStateOf("") }
    var targetMuscles by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Add New Exercise",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = exerciseName,
                    onValueChange = { exerciseName = it },
                    label = { Text("Exercise Name *") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = targetMuscles,
                    onValueChange = { targetMuscles = it },
                    label = { Text("Target Muscles") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("e.g., Pectorals, Triceps, Deltoids") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = equipment,
                    onValueChange = { equipment = it },
                    label = { Text("Equipment") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("e.g., Dumbbell, Bench, Barbell") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = exerciseType,
                    onValueChange = { exerciseType = it },
                    label = { Text("Workout Type") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("e.g., Strength, Cardio, Stretch") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "* Required field\nSeparate multiple values with commas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val workoutItem = WorkoutItem(
                        exerciseId = "custom_${Clock.System.now().epochSeconds}",
                        name = exerciseName.ifEmpty { "Custom Exercise" },
                        imageUrl = "",
                        bodyParts = emptyList(),
                        equipments = equipment.split(",").map { it.trim() }
                            .filter { it.isNotEmpty() },
                        exerciseType = exerciseType.ifEmpty { "Custom" },
                        targetMuscles = targetMuscles.split(",").map { it.trim() }
                            .filter { it.isNotEmpty() },
                        secondaryMuscles = emptyList(),
                        keywords = emptyList(),
                        isFavorite = false
                    )
                    onSaveWorkout(workoutItem)
                },
                enabled = exerciseName.isNotEmpty()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}