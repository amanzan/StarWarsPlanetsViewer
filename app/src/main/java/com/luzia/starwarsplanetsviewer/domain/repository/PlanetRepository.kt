package com.luzia.starwarsplanetsviewer.domain.repository

import com.luzia.starwarsplanetsviewer.domain.model.Planet
import kotlinx.coroutines.flow.Flow

interface PlanetRepository {
    suspend fun getPlanets(): Flow<Result<List<Planet>>>
}