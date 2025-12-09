package com.example.sotck_exchange

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sotck_exchange.ui.MainActivity
import com.example.stock_exchange.ui.main_screen.MainViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun feedToggleButton_changesFeedState() {
        // Wait for "Start Feed" button to become enabled
        composeRule.waitUntil(timeoutMillis = 5000) {
            val node = composeRule.onNodeWithText("Start Feed").fetchSemanticsNode()
            val isDisabled = SemanticsProperties.Disabled in node.config
            !isDisabled
        }

        // Tap it
        composeRule.onNodeWithText("Start Feed").performClick()

        // Wait for text to flip to "Stop Feed"
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithText("Stop Feed")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verify
        composeRule.onNodeWithText("Stop Feed").assertIsDisplayed()
    }

    @Test
    fun clickingStock_navigatesToDetailsScreen() {
        // Click first stock row
        composeRule.onAllNodes(hasTestTag("stock_row"))[0].performClick()

        // Verify details screen title
        composeRule.onNodeWithText("AAPL").assertIsDisplayed()
    }


}