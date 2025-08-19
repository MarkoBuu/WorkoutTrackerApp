package org.example.project.features.exercises.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.example.project.features.common.domain.entities.WorkoutItem
import org.example.project.features.components.ErrorContent
import org.example.project.features.components.Loader
import org.example.project.features.feed.ui.FeedUiState
import org.example.project.features.feed.ui.FeedViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExercisesRoute(
    navigateToDetail: (String) -> Unit,
    navigateToSearch: () -> Unit,
    feedViewModel: FeedViewModel = koinViewModel()
) {
    val feedUiState = feedViewModel.feedUiState.collectAsState()

    ExercisesScreen(
        feedUiState = feedUiState.value,
        navigateToSearch = navigateToSearch,
        navigateToDetail = navigateToDetail
    )
}

@Composable
fun ExercisesScreen(
    navigateToDetail: (String) -> Unit,
    feedUiState: FeedUiState,
    navigateToSearch: () -> Unit,
) {
    val workouts = feedUiState.workoutsList

    Scaffold(
        topBar = {
            TopAppBar()
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        when {
            feedUiState.isLoading -> {
                Loader()
            }
            feedUiState.isError != null -> {
                ErrorContent()
            }
            workouts != null -> {
                ExerciseContent(
                    innerPadding = innerPadding,
                    workouts = workouts,
                    navigateToDetail = navigateToDetail,
                    navigateToSearch = navigateToSearch
                )
            }
        }
    }
}

@Composable
fun ExerciseContent(
    navigateToDetail: (String) -> Unit,
    innerPadding: PaddingValues,
    workouts: List<WorkoutItem>,
    navigateToSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { navigateToSearch() },
            color = MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 2.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp))
                {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Search exercises...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                TopExercisesList(
                    title = "Top Recommendations",
                    workouts = workouts.reversed(),
                    navigateToDetail = navigateToDetail
                )
            }
            allExercises(
                title = "Newly added exercises",
                workouts = workouts,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
fun TopExercisesList(
    navigateToDetail: (String) -> Unit,
    title: String,
    workouts: List<WorkoutItem>
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(workouts, key = { it.exerciseId }) { workout ->
                ExerciseCard(
                    workout = workout,
                    modifier = Modifier.width(200.dp),
                    onClick = { navigateToDetail(workout.exerciseId) }
                )
            }
        }
    }
}

private fun LazyGridScope.allExercises(
    navigateToDetail: (String) -> Unit,
    title: String,
    workouts: List<WorkoutItem>
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }

    itemsIndexed(workouts, key = { _, it -> it.exerciseId }) { _, workout ->
        ExerciseCard(
            workout = workout,
            onClick = { navigateToDetail(workout.exerciseId) }
        )
    }
}

@Composable
fun ExerciseCard(
    workout: WorkoutItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(workout.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0.5f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                    shape = CircleShape
                ) {
                    Text(
                        text = workout.exerciseType,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar() {
    TopAppBar(
        title = {
            Text(
                "Exercises",
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

