package org.example.project.features.detail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.features.detail.data.repositories.WorkoutDetailRepository

class WorkoutDetailViewModel(
    private val workoutDetailRepository: WorkoutDetailRepository,
) : ViewModel() {

    private val _updateIsFavUiState = MutableStateFlow(WorkoutDetailUpdateIsFavoriteUiState())
    val updateIsFavUiState = _updateIsFavUiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(WorkoutDetailUiState())
    val detailUiState = _detailUiState.asStateFlow()

    fun getWorkoutDetail(exerciseId: String) {
        viewModelScope.launch {
            val workoutDetailRes = workoutDetailRepository.getWorkoutDetailList(exerciseId)
            if (workoutDetailRes.isSuccess) {
                _detailUiState.update {
                    it.copy(
                        workoutDetail = workoutDetailRes.getOrNull(),
                        isLoading = false,
                        isError = null
                    )
                }
            } else {
                _detailUiState.update {
                    it.copy(
                        isLoading = false,
                        isError = workoutDetailRes.exceptionOrNull()?.message
                    )
                }
            }
        }
    }

    fun updateIsFavorite(workoutId: String, isAdding: Boolean) {
        viewModelScope.launch {
            _updateIsFavUiState.update {
                it.copy(
                    isUpdating = true,
                    isSuccess = null,
                    isError = null
                )
            }

            try {
                if (isAdding) {
                    workoutDetailRepository.addFavorite(workoutId)
                } else {
                    workoutDetailRepository.removeFavorite(workoutId)
                }

                _detailUiState.update {
                    it.copy(
                        workoutDetail = it.workoutDetail?.copy(isFavorite = isAdding)
                    )
                }

                _updateIsFavUiState.update {
                    it.copy(
                        isUpdating = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                _updateIsFavUiState.update {
                    it.copy(
                        isUpdating = false,
                        isError = e.message
                    )
                }
            }
        }
    }
}
