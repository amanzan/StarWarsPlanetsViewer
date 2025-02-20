package com.luzia.starwarsplanetsviewer.data.repository

import com.luzia.starwarsplanetsviewer.data.local.PlanetLocalDataSource
import com.luzia.starwarsplanetsviewer.data.remote.PlanetRemoteDataSource
import com.luzia.starwarsplanetsviewer.domain.model.Planet
import com.luzia.starwarsplanetsviewer.util.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlanetRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var remoteDataSource: PlanetRemoteDataSource
    private lateinit var localDataSource: PlanetLocalDataSource
    private lateinit var repository: PlanetRepositoryImpl

    @Before
    fun setup() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        repository = PlanetRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getPlanets should emit cached data first then remote data`() = runTest {
        // Given
        val cachedPlanets = listOf(
            Planet(1, "Earth", "Temperate", "7B", "12742", "1g", "Rocky")
        )
        val remotePlanets = listOf(
            Planet(1, "Earth", "Temperate", "7B", "12742", "1g", "Rocky"),
            Planet(2, "Mars", "Cold", "0", "6779", "0.38g", "Desert")
        )

        coEvery { localDataSource.getPlanets() } returns cachedPlanets
        coEvery { remoteDataSource.getPlanets() } returns remotePlanets
        coEvery { localDataSource.savePlanets(remotePlanets) } just Runs

        // When
        val results = mutableListOf<Result<List<Planet>>>()
        repository.getPlanets().toList(results)

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0].isSuccess)
        assertEquals(cachedPlanets, results[0].getOrNull())
        assertTrue(results[1].isSuccess)
        assertEquals(remotePlanets, results[1].getOrNull())
        coVerify { localDataSource.savePlanets(remotePlanets) }
    }

    @Test
    fun `getPlanets should emit cached data when remote fails`() = runTest {
        // Given
        val cachedPlanets = listOf(
            Planet(1, "Earth", "Temperate", "7B", "12742", "1g", "Rocky")
        )
        val error = RuntimeException("Network error")

        coEvery { localDataSource.getPlanets() } returns cachedPlanets
        coEvery { remoteDataSource.getPlanets() } throws error

        // When
        val results = mutableListOf<Result<List<Planet>>>()
        repository.getPlanets().toList(results)

        // Then
        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)
        assertEquals(cachedPlanets, results[0].getOrNull())
    }
}