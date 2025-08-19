package org.example.project.features.detail.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.example.project.features.common.domain.entities.WorkoutDetailItem
import org.example.project.features.components.ErrorContent
import org.example.project.features.components.Loader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailRoute(
    exerciseId: String,
    onBackClick: () -> Unit,
    isUserLoggedIn: () -> Boolean,
    openLoginBottomSheet: (() -> Unit) -> Unit,
    detailViewModel: WorkoutDetailViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        detailViewModel.getWorkoutDetail(exerciseId)
    }

    val detailUiState = detailViewModel.detailUiState.collectAsState()

    val uriHandler = LocalUriHandler.current
    val onWatchVideoClick: (String) -> Unit = { link ->
        if (link.isNotEmpty()) {
            uriHandler.openUri(link)
        }
    }
    var showAlertDialog by remember {
        mutableStateOf(false)
    }

    val onSaveClick: (WorkoutDetailItem) -> Unit = {
        if (!isUserLoggedIn()) {
            showAlertDialog = true
        } else
            detailViewModel.updateIsFavorite(
                workoutId = it.exerciseId,
                isAdding = !it.isFavorite
            )
    }

    val updateIsFavUiState = detailViewModel.updateIsFavUiState.collectAsState()

    if (showAlertDialog) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = {
                showAlertDialog = false
            },
            title = {
                Text(
                    text = "Update Favorites",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            text = {
                Text(
                    text = "Login to Add/Remove Favorites",
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
                            detailUiState.value.workoutDetail?.let {
                                detailViewModel.updateIsFavorite(
                                    workoutId = it.exerciseId,
                                    isAdding = !it.isFavorite
                                )
                            }
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
    DetailScreen(
        uiState = detailUiState.value,
        onBackClick = onBackClick,
        onWatchVideoClick = onWatchVideoClick,
        onSaveClick = onSaveClick,
        updateIsFavUiState = updateIsFavUiState.value
    )
}

@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    uiState: WorkoutDetailUiState,
    onWatchVideoClick: (String) -> Unit,
    updateIsFavUiState: WorkoutDetailUpdateIsFavoriteUiState,
    onSaveClick: (WorkoutDetailItem) -> Unit
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    Loader()
                }

                uiState.isError != null -> {
                    ErrorContent()
                }

                uiState.workoutDetail != null -> {
                    WorkoutDetailContent(
                        workoutDetail = uiState.workoutDetail,
                        onWatchVideoClick = onWatchVideoClick,
                        onSaveClick = onSaveClick
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutDetailContent(
    workoutDetail: WorkoutDetailItem,
    onWatchVideoClick: (String) -> Unit,
    onSaveClick: (WorkoutDetailItem) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        WorkoutDetailMainContent(workoutDetail, onWatchVideoClick)

        IconButton(
            onClick = {
                onSaveClick(workoutDetail)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = if (workoutDetail.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun WorkoutDetailMainContent(
    workoutDetail: WorkoutDetailItem,
    onWatchVideoClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AsyncImage(
                    model = workoutDetail.imageUrl,
                    contentDescription = workoutDetail.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border
                            (
                            width = 1.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                )
            }

            item {
                WorkoutDetailsHeader(workoutDetail)
            }

            item {
                WorkoutDetailSection(
                    title = "Body Parts",
                    items = workoutDetail.bodyParts
                )
            }

            item {
                WorkoutDetailSection(
                    title = "Equipment",
                    items = workoutDetail.equipments
                )
            }

            item {
                WorkoutDetailSection(
                    title = "Exercise Type",
                    items = listOf(workoutDetail.exerciseType)
                )
            }

            if (workoutDetail.targetMuscles.isNotEmpty()) {
                item {
                    WorkoutDetailSection(
                        title = "Target Muscles",
                        items = workoutDetail.targetMuscles
                    )
                }
            }

            if (workoutDetail.secondaryMuscles.isNotEmpty()) {
                item {
                    WorkoutDetailSection(
                        title = "Secondary Muscles",
                        items = workoutDetail.secondaryMuscles
                    )
                }
            }

            item {
                WorkoutDetailSection(
                    title = "Instructions",
                    items = workoutDetail.instructions
                )
            }

            item {
                WorkoutDetailSection(
                    title = "Exercise Tips",
                    items = workoutDetail.exerciseTips
                )
            }

            item {
                WorkoutDetailSection(
                    title = "Variations",
                    items = workoutDetail.variations
                )
            }

            item {
                WatchVideoButton(
                    youtubeLink = workoutDetail.videoUrl,
                    onWatchVideoClick = onWatchVideoClick
                )
            }
        }
    }
}

@Composable
private fun WorkoutDetailsHeader(workoutDetail: WorkoutDetailItem) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = workoutDetail.name,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = workoutDetail.overview,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun WorkoutDetailSection(
    title: String,
    items: List<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items.forEach { item ->
                    Text(
                        text = "• $item",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun WatchVideoButton(
    youtubeLink: String,
    onWatchVideoClick: (String) -> Unit
) {
    Button(
        onClick = {
            onWatchVideoClick(youtubeLink)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Watch",
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            "Watch Video", style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Workout Details",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}