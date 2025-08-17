package org.example.project.features.favorites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        navigateToDetail = navigateToDetail
    )
}

@Composable
fun FavoritesScreen(
    uiState: FavoritesScreenUiState,
    navigateToDetail: (String) -> Unit
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
                    ErrorContent()
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
        verticalAlignment = Alignment.CenterVertically,
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
    ) {
        AsyncImage(
            model = workout.imageUrl,
            onError = { println("onError=${it.result.throwable}") },
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = imageModifier
        )

        Column {
            Text(
                textAlign = TextAlign.Left,
                text = workout.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                textAlign = TextAlign.Left,
                text = workout.exerciseType,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )

            Text(
                textAlign = TextAlign.Left,
                text = workout.equipments.joinToString(", "),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )

            Text(
                textAlign = TextAlign.Left,
                text = workout.bodyParts.joinToString(", "),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )

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
        )
    )
}