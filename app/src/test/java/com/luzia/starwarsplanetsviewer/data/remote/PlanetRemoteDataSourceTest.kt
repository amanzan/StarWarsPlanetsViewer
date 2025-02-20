package com.luzia.starwarsplanetsviewer.data.remote

import com.luzia.starwarsplanetsviewer.data.model.PlanetDto
import com.luzia.starwarsplanetsviewer.data.model.PlanetResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlanetRemoteDataSourceTest {
    private lateinit var planetApi: PlanetApi
    private lateinit var remoteDataSource: PlanetRemoteDataSource

    @Before
    fun setup() {
        planetApi = mockk()
        remoteDataSource = PlanetRemoteDataSource(planetApi)
    }

    @Test
    fun `getPlanets should map API response to domain models`() = runTest {
        // Given
        val planetDto = PlanetDto(
            url = "https://swapi.dev/api/planets/1/",
            name = "Tatooine",
            climate = "Arid",
            population = "200000",
            diameter = "10465",
            gravity = "1",
            terrain = "Desert"
        )
        val response = PlanetResponse(listOf(planetDto))
        coEvery { planetApi.getPlanets() } returns response

        // When
        val result = remoteDataSource.getPlanets()

        // Then
        assertEquals(1, result.size)
        with(result[0]) {
            assertEquals(1, id)
            assertEquals("Tatooine", name)
            assertEquals("Arid", climate)
            assertEquals("200000", population)
            assertEquals("10465", diameter)
            assertEquals("1", gravity)
            assertEquals("Desert", terrain)
        }
    }
}