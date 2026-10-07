package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.ui.PortalErrorState
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PortalErrorStateTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun testPortalErrorStateDisplaysRetryButtonAndHandlesClick() {
    var retryClicked = false
    var browserClicked = false

    composeTestRule.setContent {
      PortalErrorState(
        portalUrl = "https://one.sti.edu",
        errorMessage = "net::ERR_INTERNET_DISCONNECTED",
        onRetry = { retryClicked = true },
        onOpenInBrowser = { browserClicked = true }
      )
    }

    // Verify error title and error details are displayed
    composeTestRule.onNodeWithText("Unable to Connect").assertIsDisplayed()
    composeTestRule.onNodeWithText("net::ERR_INTERNET_DISCONNECTED").assertIsDisplayed()

    // Verify Retry button exists and is displayed
    val retryButton = composeTestRule.onNodeWithTag("retry_button")
    retryButton.assertIsDisplayed()

    // Perform click on Retry button
    retryButton.performClick()
    assertTrue("Retry button should trigger onRetry callback", retryClicked)

    // Verify Open in Browser button exists
    val browserButton = composeTestRule.onNodeWithTag("open_browser_button")
    browserButton.assertIsDisplayed()
    browserButton.performClick()
    assertTrue("Open in browser button should trigger onOpenInBrowser callback", browserClicked)
  }
}
