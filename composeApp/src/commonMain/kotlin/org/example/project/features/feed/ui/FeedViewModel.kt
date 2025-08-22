package org.example.project.features.feed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.wojciechosak.calendar.utils.today
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.example.project.features.common.domain.entities.WorkoutItem
import org.example.project.features.feed.domain.repositories.FeedRepository
import org.example.project.features.workouts.data.WorkoutSession

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
    private val _selectedDate = MutableStateFlow(LocalDate.today())
    val selectedDate = _selectedDate.asStateFlow()

    // Store workout sessions with date as key
    private val _workoutSessions = MutableStateFlow<Map<LocalDate, List<WorkoutSession>>>(emptyMap())
    val workoutSessions = _workoutSessions.asStateFlow()

    // Get workouts for selected date
    val filteredWorkouts = combine(selectedDate, workoutSessions) { date, sessions ->
        sessions[date] ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun addWorkoutSession(session: WorkoutSession) {
        _workoutSessions.update { currentSessions ->
            val existingSessions = currentSessions[session.date] ?: emptyList()
            currentSessions + (session.date to (existingSessions + session))
        }
    }

    // If you want to also store individual workout items with dates
    private val _workoutItemsWithDate = MutableStateFlow<Map<LocalDate, List<WorkoutItem>>>(emptyMap())
    val workoutItemsWithDate = _workoutItemsWithDate.asStateFlow()

    val filteredWorkoutItems = combine(selectedDate, workoutItemsWithDate) { date, itemsMap ->
        itemsMap[date] ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addWorkoutItem(date: LocalDate, workoutItem: WorkoutItem) {
        _workoutItemsWithDate.update { currentItems ->
            val existingItems = currentItems[date] ?: emptyList()
            currentItems + (date to (existingItems + workoutItem))
        }
    }
}