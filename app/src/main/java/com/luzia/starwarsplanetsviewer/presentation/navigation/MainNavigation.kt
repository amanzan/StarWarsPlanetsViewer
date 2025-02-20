package com.luzia.starwarsplanetsviewer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.luzia.starwarsplanetsviewer.presentation.ui.PlanetDetailScreen
import com.luzia.starwarsplanetsviewer.presentation.ui.PlanetListScreen
import com.luzia.starwarsplanetsviewer.presentation.viewmodel.PlanetListViewModel

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val viewModel: PlanetListViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.PlanetList.route
    ) {
        composable(NavRoutes.PlanetList.route) {
            PlanetListScreen(
                onPlanetClick = { planet ->
                    navController.navigate(NavRoutes.PlanetDetail.createRoute(planet.id))
                }
            )
        }

        composable(
            route = NavRoutes.PlanetDetail.route,
            arguments = listOf(
                navArgument("planetId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val planetId = backStackEntry.arguments?.getInt("planetId") ?: return@composable

            PlanetDetailScreen(
                viewModel = viewModel,
                planetId = planetId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}