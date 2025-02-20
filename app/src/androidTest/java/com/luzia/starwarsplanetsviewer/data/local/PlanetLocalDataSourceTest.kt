package com.luzia.starwarsplanetsviewer.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.luzia.starwarsplanetsviewer.domain.model.Planet
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class PlanetLocalDataSourceTest {
    private lateinit var database: PlanetDatabase
    private lateinit var dao: PlanetDao
    private lateinit var localDataSource: PlanetLocalDataSource

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlanetDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.planetDao()
        localDataSource = PlanetLocalDataSource(dao)
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun shouldSaveAndRetrievePlanets() = runTest {
        // Given
        val planets = listOf(
            Planet(
                id = 1,
                name = "Earth",
                climate = "Temperate",
                population = "7B",
                diameter = "12742",
                gravity = "1g",
                terrain = "Rocky"
            ),
            Planet(
                id = 2,
                name = "Mars",
                climate = "Cold",
                population = "0",
                diameter = "6779",
                gravity = "0.38g",
                terrain = "Desert"
            )
        )

        // When
        localDataSource.savePlanets(planets)
        val retrievedPlanets = localDataSource.getPlanets()

        // Then
        assertEquals(planets.size, retrievedPlanets.size)
        assertEquals(planets[0].name, retrievedPlanets[0].name)
        assertEquals(planets[1].name, retrievedPlanets[1].name)
    }

    @Test
    fun shouldReturnEmptyListWhenDatabaseIsEmpty() = runTest {
        // When
        val planets = localDataSource.getPlanets()

        // Then
        assertTrue(planets.isEmpty())
    }

    @Test
    fun shouldOverwriteExistingPlanetsOnSave() = runTest {
        // Given
        val initialPlanets = listOf(
            Planet(
                id = 1,
                name = "Earth",
                climate = "Temperate",
                population = "7B",
                diameter = "12742",
                gravity = "1g",
                terrain = "Rocky"
            )
        )

        val newPlanets = listOf(
            Planet(
                id = 2,
                name = "Mars",
                climate = "Cold",
                population = "0",
                diameter = "6779",
                gravity = "0.38g",
                terrain = "Desert"
            )
        )

        // When
        localDataSource.savePlanets(initialPlanets)
        localDataSource.savePlanets(newPlanets)
        val retrievedPlanets = localDataSource.getPlanets()

        // Then
        assertEquals(1, retrievedPlanets.size)
        assertEquals("Mars", retrievedPlanets[0].name)
    }

    @Test
    fun shouldHandleEmptyList() = runTest {
        // When
        localDataSource.savePlanets(emptyList())
        val planets = localDataSource.getPlanets()

        // Then
        assertTrue(planets.isEmpty())
    }

    @Test
    fun shouldPreserveAllPlanetFields() = runTest {
        // Given
        val planet = Planet(
            id = 1,
            name = "Earth",
            climate = "Temperate",
            population = "7B",
            diameter = "12742",
            gravity = "1g",
            terrain = "Rocky"
        )

        // When
        localDataSource.savePlanets(listOf(planet))
        val retrievedPlanet = localDataSource.getPlanets().first()

        // Then
        assertEquals(planet.id, retrievedPlanet.id)
        assertEquals(planet.name, retrievedPlanet.name)
        assertEquals(planet.climate, retrievedPlanet.climate)
        assertEquals(planet.population, retrievedPlanet.population)
        assertEquals(planet.diameter, retrievedPlanet.diameter)
        assertEquals(planet.gravity, retrievedPlanet.gravity)
        assertEquals(planet.terrain, retrievedPlanet.terrain)
    }
}