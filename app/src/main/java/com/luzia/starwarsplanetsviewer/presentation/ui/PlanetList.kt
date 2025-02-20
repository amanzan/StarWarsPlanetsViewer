package com.luzia.starwarsplanetsviewer.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.luzia.starwarsplanetsviewer.domain.model.Planet

@Composable
fun PlanetList(
    planets: List<Planet>,
    onPlanetClick: (Planet) -> Unit
) {
    LazyColumn {
        items(planets) { planet ->
            PlanetListItem(
                planet = planet,
                onClick = { onPlanetClick(planet) }
            )
        }
    }
}