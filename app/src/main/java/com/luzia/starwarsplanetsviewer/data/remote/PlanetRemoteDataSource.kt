package com.luzia.starwarsplanetsviewer.data.remote

import com.luzia.starwarsplanetsviewer.data.model.toDomain
import com.luzia.starwarsplanetsviewer.domain.model.Planet
import javax.inject.Inject

class PlanetRemoteDataSource @Inject constructor(
    private val planetApi: PlanetApi
) {
    suspend fun getPlanets(): List<Planet> {
        return planetApi.getPlanets().results.map {
            dto -> dto.toDomain()
        }
    }
}