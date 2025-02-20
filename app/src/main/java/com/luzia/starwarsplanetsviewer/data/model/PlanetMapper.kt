package com.luzia.starwarsplanetsviewer.data.model

import com.luzia.starwarsplanetsviewer.domain.model.Planet

fun PlanetDto.toDomain() = Planet(
    id = id,
    name = name,
    climate = climate,
    population = population,
    terrain = terrain,
    diameter = diameter,
    gravity = gravity
)

fun PlanetEntity.toDomain() = Planet(
    id = id,
    name = name,
    climate = climate,
    population = population,
    terrain = terrain,
    diameter = diameter,
    gravity = gravity
)

fun Planet.toEntity() = PlanetEntity(
    id = id,
    name = name,
    climate = climate,
    population = population,
    terrain = terrain,
    diameter = diameter,
    gravity = gravity
)