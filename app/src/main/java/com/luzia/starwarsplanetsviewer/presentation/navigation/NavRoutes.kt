package com.luzia.starwarsplanetsviewer.presentation.navigation

sealed class NavRoutes(val route: String) {
    object PlanetList : NavRoutes("planetList")
    object PlanetDetail : NavRoutes("planetDetail/{planetId}") {
        fun createRoute(planetId: Int) = "planetDetail/$planetId"
    }
}