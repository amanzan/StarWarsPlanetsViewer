package com.luzia.starwarsplanetsviewer.presentation.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luzia.starwarsplanetsviewer.domain.usecase.GetPlanetsUseCase
import com.luzia.starwarsplanetsviewer.presentation.ui.PlanetListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PlanetListViewModel @Inject constructor(
    private val getPlanetsUseCase: GetPlanetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlanetListUiState>(PlanetListUiState.Loading)
    val uiState: StateFlow<PlanetListUiState> = _uiState.asStateFlow()

    init {
        loadPlanets()
    }

    @VisibleForTesting
    internal fun loadPlanets() {
        viewModelScope.launch {
            getPlanetsUseCase().collect { result ->
                _uiState.value = when {
                    result.isSuccess -> PlanetListUiState.Success(result.getOrNull() ?: emptyList())
                    result.isFailure -> PlanetListUiState.Error(
                        result.exceptionOrNull()?.message ?: "Unknown error"
                    )
                    else -> PlanetListUiState.Error("Unknown error")
                }
            }
        }
    }

    fun refresh() {
        loadPlanets()
    }
}
