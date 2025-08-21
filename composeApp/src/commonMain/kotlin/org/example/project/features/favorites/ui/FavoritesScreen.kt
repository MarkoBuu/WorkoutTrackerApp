package org.example.project.features.favorites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.example.project.features.common.domain.entities.WorkoutDetailItem
import org.example.project.features.components.ErrorContent
import org.example.project.features.components.Loader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesRoute(
    favoritesScreenViewModel: FavoritesScreenViewModel = koinViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val uiState = favoritesScreenViewModel.favoritesScreenUiState.collectAsState()
    LaunchedEffect(Unit) {
        favoritesScreenViewModel.getWorkoutList()
    }

    FavoritesScreen(
        uiState = uiState.value,
        navigateToDetail = navigateToDetail,
        favoritesScreenViewModel = favoritesScreenViewModel
    )
}

@Composable
fun FavoritesScreen(
    uiState: FavoritesScreenUiState,
    navigateToDetail: (String) -> Unit,
    favoritesScreenViewModel: FavoritesScreenViewModel
) {
    val workouts = uiState.favoriteWorkoutsList
    Scaffold(
        topBar = { TopAppBar() },
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
                uiState.isLoading -> {
                    Loader()
                }

                uiState.isError != null -> {
                    ErrorContent(
                        onClick = {
                            favoritesScreenViewModel.getWorkoutList()
                        }
                    )
                }

                workouts != null -> {
                    FavoriteContent(innerPadding, workouts, navigateToDetail)
                }
            }
        }
    }
}

@Composable
fun FavoriteContent(
    innerPadding: PaddingValues,
    workouts: List<WorkoutDetailItem>,
    navigateToDetail: (String) -> Unit
) {
    val imageModifier = Modifier.width(140.dp)
        .fillMaxHeight()
        .padding(4.dp)
        .clip(RoundedCornerShape(16.dp))

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(workouts, key = {
            it.exerciseId
        }) {
            FavoriteExerciseCard(
                workout = it,
                imageModifier = imageModifier,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
fun FavoriteExerciseCard(
    workout: WorkoutDetailItem,
    modifier: Modifier = Modifier,
    imageModifier: Modifier,
    navigateToDetail: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .background(MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(12.dp))
            .border
                (
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )
            .clickable {
                navigateToDetail(workout.exerciseId)
            }
            .height(160.dp)
    ) {
        AsyncImage(
            model = workout.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = imageModifier
        )

        Column(
            modifier = Modifier
                .padding(12.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = workout.name,
                style = MaterialTheme.typography.titleMedium.copy(
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

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                shape = CircleShape
            ) {
                Text(
                    text = workout.equipments.joinToString(", "),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                shape = CircleShape
            ) {
                Text(
                    text = workout.bodyParts.joinToString(", "),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
) {
    TopAppBar(
        title = {
            Text(
                "Favorites",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        windowInsets = WindowInsets(
            top = 0,
            bottom = 0
        ),
    )
}