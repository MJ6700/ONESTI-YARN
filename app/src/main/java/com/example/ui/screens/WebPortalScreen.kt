package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.RenderProcessGoneDetail
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPortalScreen(
  targetUrl: String,
  onUrlChange: (String) -> Unit,
  onSwitchToNativeHub: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var webViewRef by remember { mutableStateOf<WebView?>(null) }
  var swipeRefreshRef by remember { mutableStateOf<SwipeRefreshLayout?>(null) }
  var canGoBack by remember { mutableStateOf(false) }
  var isLoading by remember { mutableStateOf(true) }
  var progress by remember { mutableIntStateOf(0) }
  var pageTitle by remember { mutableStateOf("ONE STI Portal") }
  var hasLoadError by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf("") }

  // Hardware/System back button navigates WebView history or returns to app
  BackHandler {
    if (canGoBack) {
      webViewRef?.goBack()
    } else {
      onSwitchToNativeHub()
    }
  }

  LaunchedEffect(targetUrl) {
    if (webViewRef != null && webViewRef?.url != targetUrl) {
      hasLoadError = false
      webViewRef?.loadUrl(targetUrl)
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF1F5F9))
  ) {
    // Clean App Bar Header
    Surface(
      color = StiPrimaryNavy,
      shadowElevation = 4.dp
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = {
              if (canGoBack) {
                webViewRef?.goBack()
              } else {
                onSwitchToNativeHub()
              }
            },
            modifier = Modifier.testTag("portal_header_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }

          Column(
            modifier = Modifier
              .weight(1f)
              .padding(start = 4.dp)
          ) {
            Text(
              text = if (pageTitle.isNotBlank() && !pageTitle.contains("http")) pageTitle else "ONE STI Portal",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              maxLines = 1
            )
            Text(
              text = "Official STI Online Services",
              color = StiAccentGold,
              fontSize = 11.sp
            )
          }

          IconButton(
            onClick = {
              if (isLoading) {
                webViewRef?.stopLoading()
                swipeRefreshRef?.isRefreshing = false
              } else {
                hasLoadError = false
                swipeRefreshRef?.isRefreshing = true
                webViewRef?.reload()
              }
            },
            modifier = Modifier.testTag("portal_header_reload_button")
          ) {
            Icon(
              imageVector = if (isLoading) Icons.Default.Close else Icons.Default.Refresh,
              contentDescription = if (isLoading) "Stop Loading" else "Reload",
              tint = Color.White
            )
          }
        }

        // Loading Progress Bar
        AnimatedVisibility(visible = isLoading && progress < 100) {
          LinearProgressIndicator(
            progress = { progress / 100f },
            modifier = Modifier
              .fillMaxWidth()
              .height(3.dp),
            color = StiAccentGold,
            trackColor = StiPrimaryNavy
          )
        }
      }
    }

    // Main Web View with Pull-To-Refresh (SwipeRefreshLayout)
    Box(
      modifier = Modifier
        .fillMaxSize()
        .weight(1f)
    ) {
      AndroidView(
        modifier = Modifier
          .fillMaxSize()
          .testTag("portal_webview"),
        factory = { ctx ->
          val webView = WebView(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
            )

            setLayerType(View.LAYER_TYPE_SOFTWARE, null)

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
              builtInZoomControls = true
              displayZoomControls = false
              cacheMode = WebSettings.LOAD_DEFAULT
              allowFileAccess = true
              mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
              userAgentString =
                "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36"
            }

            webViewClient = object : WebViewClient() {
              override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
              ): Boolean {
                val url = request?.url?.toString() ?: return false
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
              }

              override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isLoading = false
                canGoBack = view?.canGoBack() ?: false
                swipeRefreshRef?.isRefreshing = false
              }

              override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
              ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                  hasLoadError = true
                  errorMessage = error?.description?.toString() ?: "Network connection error"
                  swipeRefreshRef?.isRefreshing = false
                }
              }

              override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
              ) {
                handler?.proceed()
              }

              override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail?
              ): Boolean {
                hasLoadError = true
                errorMessage = "Display renderer restarted. Tap Retry to reload."
                swipeRefreshRef?.isRefreshing = false
                return true
              }
            }

            webChromeClient = object : WebChromeClient() {
              override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progress = newProgress
                if (newProgress >= 100) {
                  isLoading = false
                  swipeRefreshRef?.isRefreshing = false
                }
              }

              override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                title?.let {
                  if (it.isNotBlank()) pageTitle = it
                }
              }
            }

            loadUrl(targetUrl)
            webViewRef = this
          }

          SwipeRefreshLayout(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
            )
            setColorSchemeColors(
              android.graphics.Color.parseColor("#0038A8"),
              android.graphics.Color.parseColor("#FACC15"),
              android.graphics.Color.parseColor("#0B3C73")
            )

            // Only allow pull-to-refresh when the WebView is at the top of its scroll container
            setOnChildScrollUpCallback { _, _ ->
              webView.canScrollVertically(-1)
            }

            setOnRefreshListener {
              hasLoadError = false
              val currentUrl = webView.url
              if (currentUrl.isNullOrBlank() || currentUrl == "about:blank") {
                webView.loadUrl(targetUrl)
              } else {
                webView.reload()
              }
            }

            addView(webView)
            swipeRefreshRef = this
          }
        },
        update = { swipeLayout ->
          swipeRefreshRef = swipeLayout
        }
      )

      // Connection Error Fallback
      if (hasLoadError) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(16.dp)
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(60.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFFEF2F2)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.WifiOff,
                  contentDescription = "Connection Error",
                  tint = Color(0xFFEF4444),
                  modifier = Modifier.size(32.dp)
                )
              }

              Text(
                text = "Portal Unavailable",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = StiPrimaryNavy
              )

              Text(
                text = if (errorMessage.isNotBlank()) {
                  "Could not connect to the campus portal ($errorMessage). Please verify your connection."
                } else {
                  "Could not connect to the campus portal. Please verify your connection."
                },
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                lineHeight = 18.sp
              )

              Button(
                onClick = {
                  hasLoadError = false
                  swipeRefreshRef?.isRefreshing = true
                  val currentUrl = webViewRef?.url
                  if (currentUrl.isNullOrBlank() || currentUrl == "about:blank") {
                    webViewRef?.loadUrl(targetUrl)
                  } else {
                    webViewRef?.reload()
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("retry_button"),
                colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryBlue)
              ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Retry")
              }

              OutlinedButton(
                onClick = onSwitchToNativeHub,
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "Back to Home Dashboard",
                  color = StiPrimaryBlue,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }
      }
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      swipeRefreshRef?.setOnRefreshListener(null)
      webViewRef?.stopLoading()
      webViewRef?.destroy()
    }
  }
}
