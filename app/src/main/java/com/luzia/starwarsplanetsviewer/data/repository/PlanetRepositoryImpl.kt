package com.luzia.starwarsplanetsviewer.data.repository

import com.luzia.starwarsplanetsviewer.data.local.PlanetLocalDataSource
import com.luzia.starwarsplanetsviewer.data.remote.PlanetRemoteDataSource
import com.luzia.starwarsplanetsviewer.domain.model.Planet
import com.luzia.starwarsplanetsviewer.domain.repository.PlanetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PlanetRepositoryImpl @Inject constructor(
    private val remoteDataSource: PlanetRemoteDataSource,
    private val localDataSource: PlanetLocalDataSource
) : PlanetRepository {
    override suspend fun getPlanets(): Flow<Result<List<Planet>>> = flow {
        // First, emit cached data to show info instantly to the user
        val cachedPlanets = localDataSource.getPlanets()
        emit(Result.success(cachedPlanets))
        try {
            // Then try to fetch fresh data and update only if data has changed
            val remotePlanets = remoteDataSource.getPlanets()
            if (remotePlanets != cachedPlanets) {
                localDataSource.savePlanets(remotePlanets)
                emit(Result.success(remotePlanets))
            }
        } catch (e: Exception) {
            // Emit failure only if there's no cached data
            if (cachedPlanets.isEmpty()) {
                emit(Result.failure(e))
            }
        }
    }.flowOn(Dispatchers.IO)
}