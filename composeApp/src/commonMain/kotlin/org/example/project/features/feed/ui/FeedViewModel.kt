package org.example.project.features.feed.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.features.feed.domain.repositories.FeedRepository

class FeedViewModel(
    private val feedRepository: FeedRepository
): ViewModel() {

    private val _feedUiState = MutableStateFlow(FeedUiState())
    val feedUiState = _feedUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getWorkoutList()
        }
    }
    private val _apiTestResult = mutableStateOf("Ready to test")
    val apiTestResult: State<String> = _apiTestResult

    fun testApiConnection() = viewModelScope.launch {
        _apiTestResult.value = "Testing..."
        _apiTestResult.value = feedRepository.getWorkoutList()
            .fold(
                onSuccess = { items ->
                    if (items.isEmpty()) "Empty response"
                    else "Success! Found ${items.size} items"
                },
                onFailure = { e ->
                    "API Error: ${e.message ?: "Unknown error"}"
                }
            )
    }

    private suspend fun getWorkoutList(){
        val workoutList = feedRepository.getWorkoutList()
        if(workoutList.isSuccess){
            _feedUiState.value = _feedUiState.value.copy(
                workoutsList = workoutList.getOrDefault(emptyList()),
                isLoading = false,
            )
        } else {
            _feedUiState.update {
                it.copy(
                    isError = workoutList.exceptionOrNull()?.message,
                    isLoading = false,
                )
            }
        }
    }
}