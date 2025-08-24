package org.example.project.features.favorites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.features.favorites.domain.FavoriteWorkoutRepository

class FavoritesScreenViewModel(
    private val favoriteWorkoutRepository: FavoriteWorkoutRepository
): ViewModel() {

    private val _favoritesScreenUiState = MutableStateFlow(FavoritesScreenUiState())
    val favoritesScreenUiState = _favoritesScreenUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getWorkoutList()
        }
    }

    fun getWorkoutList() {
        _favoritesScreenUiState.update { it.copy(isLoading = true, isError = null) }

        viewModelScope.launch {
            val workoutList = favoriteWorkoutRepository.getAllFavoriteWorkouts()
            if(workoutList.isSuccess){
                _favoritesScreenUiState.update {
                    it.copy(
                        favoriteWorkoutsList = workoutList.getOrDefault(emptyList()),
                        isLoading = false,
                    )
                }
            } else {
                _favoritesScreenUiState.update {
                    it.copy(
                        isError = workoutList.exceptionOrNull()?.message,
                        isLoading = false,
                    )
                }
            }
        }
    }
}