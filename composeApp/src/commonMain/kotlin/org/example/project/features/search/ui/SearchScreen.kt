package org.example.project.features.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.example.project.features.common.domain.entities.WorkoutItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchRoute(
    navigateToDetail: (String) -> Unit,
    onBackPress: () -> Unit,
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val screenState = searchViewModel.searchScreenUiState.collectAsState()
    val searchText = searchViewModel.searchText.collectAsState()

    SearchScreen(
        searchText = searchText.value,
        onSearchTextChanged = {
            searchViewModel.onSearchQueryChanged(it)
        },
        searchScreenState = screenState.value,
        onWorkoutItemClicked = { navigateToDetail(it.exerciseId) },
        onBackPress = onBackPress
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    searchScreenState: SearchScreenState,
    onWorkoutItemClicked: (WorkoutItem) -> Unit,
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Search Exercises", style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPress
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchScreenContent(
                searchText = searchText,
                onSearchTextChanged = onSearchTextChanged,
                searchScreenState = searchScreenState,
                onWorkoutItemClicked = onWorkoutItemClicked
            )
        }
    }
}

@Composable
fun SearchScreenContent(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    searchScreenState: SearchScreenState,
    onWorkoutItemClicked: (WorkoutItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        OutlinedTextField(
            shape = MaterialTheme.shapes.medium,
            value = searchText,
            onValueChange = {
                onSearchTextChanged(it)
            },
            placeholder = {
                Text(text = "Search Exercise...")
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }, colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            searchScreenState.isError != null -> {
                Text(
                    text = "No exercises found",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            searchScreenState.success && searchScreenState.results.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(searchScreenState.results, key = {
                        it.exerciseId
                    }) { item ->
                        SearchWorkoutItem(
                            workout = item,
                            onWorkoutItemClicked = onWorkoutItemClicked
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchWorkoutItem(
    workout: WorkoutItem,
    onWorkoutItemClicked: (WorkoutItem) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(12.dp))
            .border
                (
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )
            .clickable {
                onWorkoutItemClicked(workout)
            }
            .height(160.dp)
    ) {
        AsyncImage(
            model = workout.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
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

