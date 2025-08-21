package org.example.project.features.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.features.common.domain.entities.WorkoutItem
import org.example.project.features.search.domain.repo.SearchWorkoutRepository

class SearchViewModel(
    private val searchWorkoutRepository: SearchWorkoutRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchScreenUiState = MutableStateFlow(SearchScreenState())
    val searchScreenUiState = _searchScreenUiState.asStateFlow()

    init {
        triggerFetchItems()
    }

    private suspend fun fetchItems(query: String): List<WorkoutItem> {
        if (query.isEmpty()) return emptyList()
        val results = searchWorkoutRepository.searchWorkoutByText(query)
        return results.getOrNull() ?: emptyList()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun triggerFetchItems() = viewModelScope.launch {
        _searchText
            .debounce(500)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                flow {
                    val results = fetchItems(query)
                    emit(results)
                }
            }
            .catch { error ->
                _searchScreenUiState.update {
                    it.copy(
                        idle = false,
                        isError = error.message
                    )
                }
            }
            .collect { results ->

                _searchScreenUiState.update {
                    it.copy(
                        idle = false,
                        success = true,
                        results = results
                    )
                }
            }
    }

    fun onSearchQueryChanged(query: String) {
        _searchText.value = query
    }
}