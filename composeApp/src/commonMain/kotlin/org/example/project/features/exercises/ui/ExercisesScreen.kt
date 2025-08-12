package org.example.project.features.exercises.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.example.project.features.common.domain.entities.WorkoutItem
import org.example.project.features.components.ErrorContent
import org.example.project.features.components.Loader
import org.example.project.features.components.SearchBar
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
        topBar = { TopAppBar() },
    ) { innerPadding ->
        when {
            feedUiState.isLoading -> {
                Loader()
            }

            feedUiState.isError != null -> {
                ErrorContent()
            }

            workouts != null -> {
                ExerciseContent(innerPadding = innerPadding, workouts = workouts, navigateToDetail = navigateToDetail)
            }
        }

    }
}

@Composable
fun ExerciseContent(
    navigateToDetail: (String) -> Unit,
    innerPadding: PaddingValues,
    workouts: List<WorkoutItem>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(
            top = innerPadding.calculateTopPadding(),
        )

    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            TopExercisesList(title = "Top Recommendations", workouts = workouts.reversed(), navigateToDetail = navigateToDetail)
        }
        allExercises(
            title = "All exercises", workouts = workouts, navigateToDetail = navigateToDetail
        )
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
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(top = 16.dp, start = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                workouts, key = { it.exerciseId }) { workout ->
                val imageModifier = Modifier.width(120.dp)
                    .height(140.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                ExerciseCard(
                    workout,
                    modifier = Modifier.width(100.dp),
                    imageModifier = imageModifier.clickable {
                        navigateToDetail(workout.exerciseId)
                    }
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
    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(top = 16.dp, start = 16.dp)
        )
    }

    itemsIndexed(workouts, key = { _, it -> it.exerciseId }) { index, workout ->
        val cardPaddingStart = if(index % 2 == 0) 16.dp else 0.dp
        val cardPaddingEnd = if(index % 2 == 0) 0.dp else 16.dp
        val imageModifier = Modifier.fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
        ExerciseCard(
            workout,
            modifier = Modifier.padding(start = cardPaddingStart, end = cardPaddingEnd),
            imageModifier = imageModifier.clickable {
                navigateToDetail(workout.exerciseId)
            }
        )
    }
}

@Composable
fun ExerciseCard(
    workout: WorkoutItem,
    modifier: Modifier,
    imageModifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(12.dp))
            .border
                (
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )
    ) {

        AsyncImage(
            model = workout.imageUrl,
            onError = { println("onError=${it.result.throwable}") },
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = imageModifier
        )

        Text(
            textAlign = TextAlign.Center,
            text = workout.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                .padding(horizontal = 4.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = workout.exerciseType,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun TopAppBar() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Hi Marko!", color = MaterialTheme.colorScheme.primaryContainer,
            style = MaterialTheme.typography.titleMedium
        )

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(45.dp)
                .background(MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(12.dp))
                .border
                    (
                    width = 1.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
                .padding(horizontal = 16.dp)
        )
    }
}