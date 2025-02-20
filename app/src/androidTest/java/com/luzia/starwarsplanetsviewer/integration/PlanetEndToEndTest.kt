package com.luzia.starwarsplanetsviewer.integration

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.luzia.starwarsplanetsviewer.data.local.PlanetDatabase
import com.luzia.starwarsplanetsviewer.presentation.activity.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PlanetEndToEndTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var planetDatabase: PlanetDatabase

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking {
            planetDatabase.clearAllTables()
        }
    }

    @Test
    fun shouldDisplayPlanetsAndNavigateToDetail() {
        // Wait for planets to load
        Thread.sleep(1000)

        // Verify list screen shows planets
        composeTestRule.onNodeWithText("Tatooine").assertExists()

        // Click on a planet
        composeTestRule.onNodeWithText("Tatooine").performClick()

        // Verify detail screen shows planet information
        composeTestRule.onNodeWithText("Climate").assertExists()
        composeTestRule.onNodeWithText("arid").assertExists()
        composeTestRule.onNodeWithText("Population").assertExists()

        // Test back navigation
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Verify we're back at the list
        composeTestRule.onNodeWithText("Tatooine").assertExists()
    }
}