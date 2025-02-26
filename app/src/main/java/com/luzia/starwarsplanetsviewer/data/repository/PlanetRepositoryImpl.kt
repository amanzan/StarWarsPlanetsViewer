package com.luzia.starwarsplanetsviewer.data.repository

import com.luzia.starwarsplanetsviewer.data.local.PlanetLocalDataSource
import com.luzia.starwarsplanetsviewer.data.remote.NetworkResult
import com.luzia.starwarsplanetsviewer.data.remote.NoConnectivityException
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
        var cachedPlanets: List<Planet> = emptyList()
        try {
            cachedPlanets = localDataSource.getPlanets()
            // Emit cached data if available
            if (cachedPlanets.isNotEmpty()) {
                emit(Result.success(cachedPlanets))
            }
        } catch (e: Exception) {
            // Log cache error but don't emit yet
            e.printStackTrace()
        }

        // Then try to fetch from remote
        when (val networkResult = remoteDataSource.getPlanets()) {
            is NetworkResult.Success -> {
                val remotePlanets = networkResult.data
                if (remotePlanets != cachedPlanets) {
                    // Save to local database
                    localDataSource.savePlanets(networkResult.data)
                    // Emit updated data
                    emit(Result.success(networkResult.data))
                }
            }
            is NetworkResult.Error -> {
                // Handle HTTP errors
                emit(Result.failure(Exception("Network error: ${networkResult.code} - ${networkResult.message}")))
            }
            is NetworkResult.NoConnection -> {
                // Handle HTTP errors
                emit(Result.failure(Exception("Network error: ${networkResult.message}")))
            }
            is NetworkResult.Exception -> {
                // Handle exceptions including NoConnectivityException
                val errorMessage = if (networkResult.e is NoConnectivityException) {
                    "No internet connection. Please check your connection and try again."
                } else {
                    "An error occurred: ${networkResult.e.message}"
                }
                emit(Result.failure(Exception(errorMessage)))
            }
            is NetworkResult.Loading -> {
                // No action needed for loading state
            }
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
//}