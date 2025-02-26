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
    override fun getPlanets(): Flow<Result<List<Planet>>> = flow {
        // Start with loading from cache
        try {
            val cachedPlanets = localDataSource.getPlanets()
            // Emit cached data if available
            if (cachedPlanets.isNotEmpty()) {
                emit(Result.success(cachedPlanets))
            }
        } catch (e: Exception) {
            // Log cache error but don't emit yet
            e.printStackTrace()
        }

        // Then try to fetch from remote
        try {
            val remotePlanets = remoteDataSource.getPlanets()
            // Save to local database
            localDataSource.savePlanets(remotePlanets)
            // Emit updated data
            emit(Result.success(remotePlanets))
        } catch (e: Exception) {
            // Now we emit the error if we couldn't get remote data
            // Try to get from cache as a fallback if we haven't emitted it yet
            val cachedPlanets = try {
                localDataSource.getPlanets()
            } catch (_: Exception) {
                emptyList()
            }

            if (cachedPlanets.isNotEmpty()) {
                emit(Result.success(cachedPlanets))
            } else {
                emit(Result.failure(e))
            }
        }
    }


//        // First, emit cached data to show info instantly to the user
//        val cachedPlanets = localDataSource.getPlanets()
//        emit(Result.success(cachedPlanets))
//        try {
//            // Then try to fetch fresh data and update only if data has changed
//            val remotePlanets = remoteDataSource.getPlanets()
//            if (remotePlanets != cachedPlanets) {
//                localDataSource.savePlanets(remotePlanets)
//                emit(Result.success(remotePlanets))
//            }
//        } catch (e: Exception) {
//            // Emit failure only if there's no cached data
//            if (cachedPlanets.isEmpty()) {
//                emit(Result.failure(e))
//            }
//        }
//    }.flowOn(Dispatchers.IO)
}