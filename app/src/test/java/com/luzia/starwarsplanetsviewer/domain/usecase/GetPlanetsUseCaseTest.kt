package com.luzia.starwarsplanetsviewer.domain.usecase

import com.luzia.starwarsplanetsviewer.domain.model.Planet
import com.luzia.starwarsplanetsviewer.domain.repository.PlanetRepository
import com.luzia.starwarsplanetsviewer.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetPlanetsUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var planetRepository: PlanetRepository
    private lateinit var getPlanetsUseCase: GetPlanetsUseCase

    @Before
    fun setup() {
        planetRepository = mockk()
        getPlanetsUseCase = GetPlanetsUseCase(planetRepository)
    }

    @Test
    fun `invoke should return success with planets when repository succeeds`() = runTest {
        // Given
        val planets = listOf(
            Planet(1, "Earth", "Temperate", "7B", "12742", "1g", "Rocky"),
            Planet(2, "Mars", "Cold", "0", "6779", "0.38g", "Desert")
        )
        coEvery { planetRepository.getPlanets() } returns flowOf(Result.success(planets))

        // When
        val result = getPlanetsUseCase().first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(planets, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Given
        val error = RuntimeException("Network error")
        coEvery { planetRepository.getPlanets() } returns flowOf(Result.failure(error))

        // When
        val result = getPlanetsUseCase().first()

        // Then
        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }
}