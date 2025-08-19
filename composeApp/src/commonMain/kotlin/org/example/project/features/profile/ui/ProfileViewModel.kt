package org.example.project.features.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.project.features.profile.data.User

class ProfileViewModel : ViewModel() {

    private var _refresh = MutableStateFlow(false)
    private val refresh = _refresh.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val profileUiState: StateFlow<ProfileScreenUiState> = combine(refresh) { _ ->
        getUserInfo()
    }.flatMapLatest { it }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileScreenUiState()
    )

    private fun getUserInfo(): Flow<ProfileScreenUiState> = flow {
        emit(
            ProfileScreenUiState(
                isLoading = true
            )
        )

        delay(1000)

        emit(
            ProfileScreenUiState(
                userInfo = User(
                    id = 1,
                    name = "Marko",
                    email = "marko.budak33@gmail.com",
                    myWorkoutsCount = 10,
                    favoriteWorkoutCount = 10,
                    followers = 200,
                ),
                isLoading = false
            )
        )
    }

    fun refresh() {
        _refresh.value = true
    }
}