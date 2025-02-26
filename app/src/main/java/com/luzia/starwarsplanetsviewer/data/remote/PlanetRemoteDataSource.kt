package com.luzia.starwarsplanetsviewer.data.remote

import com.luzia.starwarsplanetsviewer.data.model.toDomain
import com.luzia.starwarsplanetsviewer.domain.model.Planet
import javax.inject.Inject

class PlanetRemoteDataSource @Inject constructor(
    private val planetApi: PlanetApi
) {
    suspend fun getPlanets(): NetworkResult<List<Planet>> {
        return try {
            val response = planetApi.getPlanets()
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()?.results?.map { it.toDomain() } ?: emptyList())
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: NoConnectivityException) {
            NetworkResult.NoConnection(e.message ?: "No internet connection")
        } catch (e: retrofit2.HttpException) {
            NetworkResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }
}