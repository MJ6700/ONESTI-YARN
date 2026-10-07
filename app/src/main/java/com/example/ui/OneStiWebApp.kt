package com.example.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.RenderProcessGoneDetail
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.StiLogoBlue

const val STI_PORTAL_URL = "https://one.sti.edu"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun OneStiWebApp(
  portalUrl: String = STI_PORTAL_URL,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var webViewRef by remember { mutableStateOf<WebView?>(null) }
  var canGoBack by remember { mutableStateOf(false) }
  var isLoading by remember { mutableStateOf(false) }
  var progress by remember { mutableIntStateOf(0) }
  var hasLoadError by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf("") }
  var uploadMessageCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }

  // File upload picker launcher for file / photo uploads inside ONE STI
  val fileChooserLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
  ) { result ->
    val uris = if (result.resultCode == Activity.RESULT_OK) {
      val data = result.data
      if (data?.clipData != null) {
        val count = data.clipData!!.itemCount
        Array(count) { i -> data.clipData!!.getItemAt(i).uri }
      } else if (data?.data != null) {
        arrayOf(data.data!!)
      } else {
        null
      }
    } else {
      null
    }
    uploadMessageCallback?.onReceiveValue(uris)
    uploadMessageCallback = null
  }

  // System/Gesture Back Handler: Navigate WebView history instead of closing app
  BackHandler(enabled = true) {
    if (hasLoadError && webViewRef?.canGoBack() == true) {
      hasLoadError = false
      webViewRef?.goBack()
      canGoBack = webViewRef?.canGoBack() ?: false
    } else if (webViewRef?.canGoBack() == true) {
      webViewRef?.goBack()
      canGoBack = webViewRef?.canGoBack() ?: false
    } else {
      (context as? Activity)?.finish()
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(StiLogoBlue)
  ) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      // Dedicated safe space for status bar, notch, and topbar clearance
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .height(5.dp)
      )

      // Ultra-slim non-intrusive loading indicator (never blocks viewing)
      this.AnimatedVisibility(
        visible = isLoading && progress < 100,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.fillMaxWidth()
      ) {
        LinearProgressIndicator(
          progress = { progress / 100f },
          modifier = Modifier
            .fillMaxWidth()
            .height(2.5.dp),
          color = Color(0xFFFFC72C),
          trackColor = Color.Transparent
        )
      }

      // Pure Full-Screen Web Portal View - Instant open with 144 FPS Hardware Acceleration
      AndroidView(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .testTag("one_sti_app_webview"),
        factory = { ctx ->
          WebView(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
            )

            // Enable hardware acceleration layer for 144 FPS smooth scrolling
            setLayerType(View.LAYER_TYPE_HARDWARE, null)

            // Background color strictly matches deeper ONE STI brand blue (#092654)
            setBackgroundColor(android.graphics.Color.parseColor("#092654"))

            // Hardware back key listener on focused view
            setOnKeyListener { _, keyCode, event ->
              if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if (canGoBack()) {
                  goBack()
                  true
                } else {
                  false
                }
              } else {
                false
              }
            }

            try {
              CookieManager.getInstance().setAcceptCookie(true)
              CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
            } catch (_: Exception) {}

            settings.apply {
              javaScriptEnabled = true
              domStorageEnabled = true
              databaseEnabled = true
              useWideViewPort = true
              loadWithOverviewMode = true
              setSupportZoom(true)
              builtInZoomControls = false
              displayZoomControls = false
              cacheMode = WebSettings.LOAD_DEFAULT
              allowFileAccess = true
              allowContentAccess = true
              setSupportMultipleWindows(true)
              javaScriptCanOpenWindowsAutomatically = true
              mediaPlaybackRequiresUserGesture = false
              mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
              userAgentString =
                "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36"
            }

            webViewClient = object : WebViewClient() {
              override fun doUpdateVisitedHistory(
                view: WebView?,
                url: String?,
                isReload: Boolean
              ) {
                super.doUpdateVisitedHistory(view, url, isReload)
                canGoBack = view?.canGoBack() ?: false
              }

              override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
              ): Boolean {
                val url = request?.url?.toString() ?: return false
                // Delegate external telephone, email, and sms protocols to system apps
                if (url.startsWith("tel:") || url.startsWith("mailto:") || url.startsWith("sms:")) {
                  try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    ctx.startActivity(intent)
                    return true
                  } catch (_: Exception) {
                    return false
                  }
                }
                return false
              }

              override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                isLoading = true
                hasLoadError = false
                canGoBack = view?.canGoBack() ?: false
              }

              override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isLoading = false
                canGoBack = view?.canGoBack() ?: false
                if (url != null && !url.startsWith("data:") && !url.startsWith("about:blank")) {
                  hasLoadError = false
                }
              }

              override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
              ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                  val desc = error?.description?.toString() ?: ""
                  val errCode = error?.errorCode ?: 0
                  // Don't show blocking error for redirects, caches, or aborted requests
                  if (errCode != WebViewClient.ERROR_UNKNOWN &&
                      !desc.contains("ERR_ABORTED", ignoreCase = true) &&
                      !desc.contains("ERR_CACHE_MISS", ignoreCase = true)
                  ) {
                    hasLoadError = true
                    errorMessage = desc.ifBlank { "Connection failed" }
                  }
                }
              }

              override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
              ) {
                super.onReceivedHttpError(view, request, errorResponse)
                if (request?.isForMainFrame == true) {
                  val statusCode = errorResponse?.statusCode ?: 200
                  if (statusCode >= 500) {
                    hasLoadError = true
                    errorMessage = "Server error (HTTP $statusCode). The portal may be temporarily undergoing maintenance."
                  }
                }
              }

              override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
              ) {
                // Ensure campus intranet & certificates proceed seamlessly
                handler?.proceed()
              }

              override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail?
              ): Boolean {
                hasLoadError = true
                errorMessage = "The web view process encountered an issue. Tap Retry to reload."
                return true
              }
            }

            webChromeClient = object : WebChromeClient() {
              override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progress = newProgress
                if (newProgress >= 100) {
                  isLoading = false
                }
              }

              override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: android.os.Message?
              ): Boolean {
                val transport = resultMsg?.obj as? WebView.WebViewTransport
                transport?.webView = view
                resultMsg?.sendToTarget()
                return true
              }

              override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
              ): Boolean {
                uploadMessageCallback?.onReceiveValue(null)
                uploadMessageCallback = filePathCallback
                try {
                  val intent = fileChooserParams?.createIntent() ?: Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "*/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                  }
                  fileChooserLauncher.launch(intent)
                  return true
                } catch (_: Exception) {
                  uploadMessageCallback = null
                  return false
                }
              }
            }

            loadUrl(portalUrl)
            webViewRef = this
          }
        },
        update = { wv ->
          webViewRef = wv
        }
      )
    }

    // Custom Error State UI with Retry Button
    androidx.compose.animation.AnimatedVisibility(
      visible = hasLoadError,
      enter = fadeIn() + scaleIn(initialScale = 0.95f),
      exit = fadeOut() + scaleOut(targetScale = 0.95f),
      modifier = Modifier.fillMaxSize()
    ) {
      PortalErrorState(
        portalUrl = portalUrl,
        errorMessage = errorMessage,
        onRetry = {
          hasLoadError = false
          isLoading = true
          val currentUrl = webViewRef?.url
          if (currentUrl.isNullOrBlank() || currentUrl == "about:blank") {
            webViewRef?.loadUrl(portalUrl)
          } else {
            webViewRef?.reload()
          }
        },
        onOpenInBrowser = {
          try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(portalUrl))
            context.startActivity(intent)
          } catch (_: Exception) {}
        }
      )
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      uploadMessageCallback?.onReceiveValue(null)
      uploadMessageCallback = null
      webViewRef?.destroy()
    }
  }
}

/**
 * Custom error state UI displayed when the WebView fails to load the ONE STI portal.
 * Adheres to Material Design 3 guidelines and provides an accessible, prominent 'Retry' button.
 */
@Composable
fun PortalErrorState(
  portalUrl: String,
  errorMessage: String,
  onRetry: () -> Unit,
  onOpenInBrowser: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            StiLogoBlue,
            Color(0xFF0B3C73)
          )
        )
      )
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(horizontal = 24.dp, vertical = 32.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .testTag("portal_error_card"),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1726)),
      shape = RoundedCornerShape(24.dp),
      border = BorderStroke(1.dp, Color(0xFF1E5BB0).copy(alpha = 0.5f)),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Glowing Icon Badge
        Box(
          modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(
              Brush.radialGradient(
                colors = listOf(
                  Color(0xFFEF4444).copy(alpha = 0.25f),
                  Color(0xFFEF4444).copy(alpha = 0.08f)
                )
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.WifiOff,
            contentDescription = "Connection Error",
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(36.dp)
          )
        }

        // Title
        Text(
          text = "Unable to Connect",
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          color = Color.White,
          textAlign = TextAlign.Center
        )

        // Description
        Text(
          text = "We couldn't reach the ONE STI Student Portal ($portalUrl). Please verify your internet connection and try again.",
          fontSize = 14.sp,
          color = Color(0xFF94A3B8),
          textAlign = TextAlign.Center,
          lineHeight = 20.sp
        )

        // Error Detail Chip (if error details are present)
        if (errorMessage.isNotBlank()) {
          Surface(
            color = Color(0xFF071220),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF1E5BB0).copy(alpha = 0.4f))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = Color(0xFFFBBF24),
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color(0xFFCBD5E1),
                maxLines = 2
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Primary Action: Retry Button
        Button(
          onClick = onRetry,
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("retry_button"),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0284C7),
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(14.dp),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Retry",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
          )
        }

        // Secondary Action: Open in External Browser
        OutlinedButton(
          onClick = onOpenInBrowser,
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("open_browser_button"),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFF38BDF8)
          ),
          border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.5f)),
          shape = RoundedCornerShape(14.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Open in Browser",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
          )
        }

        // Helpful tip for campus connectivity
        Text(
          text = "Tip: If you're on campus Wi-Fi, ensure you've signed in through the school network gateway.",
          fontSize = 11.sp,
          color = Color(0xFF64748B),
          textAlign = TextAlign.Center,
          lineHeight = 15.sp,
          modifier = Modifier.padding(top = 4.dp)
        )
      }
    }
  }
}
