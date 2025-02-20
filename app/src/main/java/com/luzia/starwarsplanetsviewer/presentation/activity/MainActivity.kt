package com.luzia.starwarsplanetsviewer.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.luzia.starwarsplanetsviewer.presentation.navigation.MainNavigation
import com.luzia.starwarsplanetsviewer.presentation.theme.StarWarsPlanetsViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarWarsPlanetsViewerTheme {
                MainNavigation()
            }
        }
    }
}