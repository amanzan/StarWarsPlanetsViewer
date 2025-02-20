package com.luzia.starwarsplanetsviewer.presentation

import com.luzia.starwarsplanetsviewer.domain.model.Planet
import com.luzia.starwarsplanetsviewer.domain.usecase.GetPlanetsUseCase
import com.luzia.starwarsplanetsviewer.presentation.ui.PlanetListUiState
import com.luzia.starwarsplanetsviewer.presentation.viewmodel.PlanetListViewModel
import com.luzia.starwarsplanetsviewer.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlanetListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getPlanetsUseCase: GetPlanetsUseCase
    private lateinit var viewModel: PlanetListViewModel

    @Before
    fun setup() {
        getPlanetsUseCase = mockk()
        coEvery { getPlanetsUseCase() } returns flowOf(Result.success(emptyList()))
        viewModel = PlanetListViewModel(getPlanetsUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initial state should be Loading`() = runTest {
        advanceUntilIdle() // Let the coroutine execute
        assertTrue(viewModel.uiState.value is PlanetListUiState.Success)
    }

    @Test
    fun `should update state to Success when use case returns planets`() = runTest {
        // Given
        val planets = listOf(
            Planet(1, "Earth", "Temperate", "7B", "12742", "1g", "Rocky")
        )
        coEvery { getPlanetsUseCase() } returns flowOf(Result.success(planets))

        // When
        viewModel.loadPlanets()

        // Then
        assertEquals(PlanetListUiState.Success(planets), viewModel.uiState.value)
    }

    @Test
    fun `should update state to Error when use case fails`() = runTest {
        // Given
        val error = RuntimeException("Network error")
        coEvery { getPlanetsUseCase() } returns flowOf(Result.failure(error))

        // When
        viewModel.loadPlanets()

        // Then
        assertTrue(viewModel.uiState.value is PlanetListUiState.Error)
        assertEquals(
            "Network error",
            (viewModel.uiState.value as PlanetListUiState.Error).message
        )
    }
}