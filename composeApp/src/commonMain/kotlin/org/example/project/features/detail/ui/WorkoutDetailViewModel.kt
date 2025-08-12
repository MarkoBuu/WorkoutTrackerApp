package org.example.project.features.detail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.features.detail.data.repositories.WorkoutDetailRepository

class WorkoutDetailViewModel(
    private val workoutDetailRepository: WorkoutDetailRepository,
): ViewModel() {

    private var _detailUiState = MutableStateFlow(WorkoutDetailUiState())
    val detailUiState = _detailUiState.asStateFlow()

    suspend fun getWorkoutDetail(exerciseId: String){
        viewModelScope.launch {
            val workoutDetailRes = workoutDetailRepository.getWorkoutDetailList(exerciseId)
            if (workoutDetailRes.isSuccess){
                _detailUiState.value = _detailUiState.value.copy(
                    workoutDetail = workoutDetailRes.getOrNull(),
                    isLoading = false,
                )
            } else {
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    isError = workoutDetailRes.exceptionOrNull()?.message
                )
            }
        }
    }
}