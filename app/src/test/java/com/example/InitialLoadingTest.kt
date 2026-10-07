package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class InitialLoadingTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun testCircularProgressIndicatorDisplaysWhenLoading() {
    val isInitialLoading = true

    composeTestRule.setContent {
      AnimatedVisibility(visible = isInitialLoading) {
        Box {
          CircularProgressIndicator(
            modifier = Modifier
              .size(56.dp)
              .testTag("circular_progress_indicator")
          )
        }
      }
    }

    composeTestRule
      .onNodeWithTag("circular_progress_indicator")
      .assertIsDisplayed()
  }
}
