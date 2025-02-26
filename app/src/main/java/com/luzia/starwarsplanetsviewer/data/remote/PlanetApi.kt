package com.luzia.starwarsplanetsviewer.data.remote

import com.luzia.starwarsplanetsviewer.data.model.PlanetResponse
import retrofit2.Response
import retrofit2.http.GET

interface PlanetApi {
    @GET("planets/")
    suspend fun getPlanets(): Response<PlanetResponse>
}