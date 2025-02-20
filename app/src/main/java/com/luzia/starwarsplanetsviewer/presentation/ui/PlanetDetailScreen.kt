package com.luzia.starwarsplanetsviewer.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luzia.starwarsplanetsviewer.presentation.viewmodel.PlanetListViewModel

@Composable
fun PlanetDetailScreen(
    viewModel: PlanetListViewModel = hiltViewModel(),
    planetId: Int,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState() // Collect UI state

    val planet = (uiState as? PlanetListUiState.Success)?.planets?.find { it.id == planetId }

    planet?.let { planetData ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = planetData.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            DetailItem("Climate", planetData.climate)
            DetailItem("Population", planetData.population)
            DetailItem("Diameter", planetData.diameter)
            DetailItem("Gravity", planetData.gravity)
            DetailItem("Terrain", planetData.terrain)
        }
    } ?: LoadingScreen()
}