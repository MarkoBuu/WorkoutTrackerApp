package org.example.project.features.feed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun getWorkoutList(){
        _feedUiState.update { it.copy(isLoading = true, isError = null) }
        viewModelScope.launch {
            val workoutList = feedRepository.getWorkoutList()
            if(workoutList.isSuccess){
                _feedUiState.update {
                    it.copy(
                        workoutsList = workoutList.getOrDefault(emptyList()),
                        isLoading = false,
                        isError = null
                    )
                }
            } else {
                _feedUiState.update {
                    it.copy(
                        isError = workoutList.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}