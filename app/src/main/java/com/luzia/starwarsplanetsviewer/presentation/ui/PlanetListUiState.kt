package com.luzia.starwarsplanetsviewer.presentation.ui

import com.luzia.starwarsplanetsviewer.domain.model.Planet

sealed class PlanetListUiState {
    object Loading : PlanetListUiState()
    data class Success(val planets: List<Planet>) : PlanetListUiState()
    data class Error(val message: String) : PlanetListUiState()
}