package com.luzia.starwarsplanetsviewer.domain.usecase

import com.luzia.starwarsplanetsviewer.domain.model.Planet
import com.luzia.starwarsplanetsviewer.domain.repository.PlanetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlanetsUseCase @Inject constructor(
    private val repository: PlanetRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Planet>>> = repository.getPlanets()
}