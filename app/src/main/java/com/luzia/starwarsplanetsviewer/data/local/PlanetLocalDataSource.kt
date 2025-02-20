package com.luzia.starwarsplanetsviewer.data.local

import com.luzia.starwarsplanetsviewer.data.model.toDomain
import com.luzia.starwarsplanetsviewer.data.model.toEntity
import com.luzia.starwarsplanetsviewer.domain.model.Planet
import javax.inject.Inject

class PlanetLocalDataSource @Inject constructor(
    private val planetDao: PlanetDao
) {
    suspend fun getPlanets(): List<Planet> {
        return planetDao.getAllPlanets().map {
            entity -> entity.toDomain()
        }
    }

    suspend fun savePlanets(planets: List<Planet>) {
        val entities = planets.map {
            planet -> planet.toEntity()
        }

        planetDao.clearPlanets()
        planetDao.insertPlanets(entities)
    }
}