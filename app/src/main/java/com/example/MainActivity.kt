package com.example

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface as ComposeSurface
import androidx.compose.ui.Modifier
import com.example.ui.OneStiWebApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StiLogoBlue

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setupHighRefreshRate()
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        ComposeSurface(
          modifier = Modifier.fillMaxSize(),
          color = StiLogoBlue
        ) {
          OneStiWebApp()
        }
      }
    }
  }

  /**
   * Configures display mode and frame pacing for 144 Hz refresh rate and 144 FPS fluid rendering.
   */
  private fun setupHighRefreshRate() {
    try {
      // 1. Request 144Hz preferred refresh rate on window layout params
      val layoutParams = window.attributes
      @Suppress("DEPRECATION")
      layoutParams.preferredRefreshRate = 144f

      // 2. Select display mode with highest refresh rate (targeting 144Hz/120Hz+)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          display
        } else {
          @Suppress("DEPRECATION")
          window.windowManager.defaultDisplay
        }
        val modes = display?.supportedModes ?: emptyArray()
        val highestMode = modes.maxByOrNull { it.refreshRate }
        if (highestMode != null && highestMode.refreshRate > 60f) {
          layoutParams.preferredDisplayModeId = highestMode.modeId
        }
      }
      window.attributes = layoutParams

      // 3. Frame rate hint on decorView for 144 FPS rendering (via reflection for compatibility)
      try {
        val method = window.decorView.javaClass.getMethod(
          "setFrameRate",
          Float::class.javaPrimitiveType,
          Int::class.javaPrimitiveType
        )
        method.invoke(window.decorView, 144f, 0)
      } catch (_: Throwable) {
        // Ignored on platforms without view-level frame rate control
      }
    } catch (_: Throwable) {
      // Gracefully continue with device default
    }
  }
}
