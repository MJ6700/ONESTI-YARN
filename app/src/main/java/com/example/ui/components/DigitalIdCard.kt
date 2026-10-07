package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy
import com.example.ui.theme.StiSuccess

@Composable
fun DigitalIdCard(
  profile: StudentProfile?,
  modifier: Modifier = Modifier,
  onTapToScan: () -> Unit = {}
) {
  var isFlipped by remember { mutableStateOf(false) }

  val rotation by animateFloatAsState(
    targetValue = if (isFlipped) 180f else 0f,
    animationSpec = tween(durationMillis = 500),
    label = "cardFlip"
  )

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(260.dp)
        .graphicsLayer {
          rotationY = rotation
          cameraDistance = 12f * density
        }
        .clickable { isFlipped = !isFlipped }
        .testTag("digital_id_card"),
      shape = RoundedCornerShape(20.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      if (rotation <= 90f) {
        // Front of ID Card
        IdCardFront(profile = profile)
      } else {
        // Back of ID Card (rotated 180 so it appears upright)
        Box(
          modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { rotationY = 180f }
        ) {
          IdCardBack(profile = profile)
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = Icons.Default.Flip,
        contentDescription = "Tap to flip",
        tint = Color(0xFF6C7A8E),
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = if (isFlipped) "Tap card to view Front" else "Tap card to view Back & Emergency Info",
        fontSize = 12.sp,
        color = Color(0xFF6C7A8E),
        fontWeight = FontWeight.Medium
      )
    }
  }
}

@Composable
private fun IdCardFront(profile: StudentProfile?) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF2F6FC)
          )
        )
      )
  ) {
    // Top STI Brand Stripe
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(58.dp)
        .background(
          brush = Brush.horizontalGradient(
            colors = listOf(
              StiPrimaryNavy,
              StiPrimaryBlue
            )
          )
        )
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = StiAccentGold,
            modifier = Modifier.size(32.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                text = "STI",
                fontWeight = FontWeight.Black,
                color = StiPrimaryNavy,
                fontSize = 14.sp
              )
            }
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "STI COLLEGE",
              color = Color.White,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 13.sp,
              letterSpacing = 0.5.sp
            )
            Text(
              text = profile?.campus?.uppercase() ?: "GLOBAL CITY CAMPUS",
              color = StiAccentGold,
              fontSize = 9.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0x33FFFFFF)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Nfc,
              contentDescription = "RFID",
              tint = StiAccentGold,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "RFID PASS",
              color = Color.White,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // Main Card Body
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = 66.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Photo Avatar
        Box(
          modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, StiAccentGold, RoundedCornerShape(12.dp))
            .background(Color(0xFFE2EAF4)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Student Photo",
            tint = StiPrimaryNavy,
            modifier = Modifier.size(46.dp)
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Student Info Details
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = profile?.fullName ?: "ALEXANDER M. SANTOS",
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            color = StiPrimaryNavy,
            maxLines = 1
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "SN: ${profile?.studentNumber ?: "02000284729"}",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = StiPrimaryBlue
          )
          Text(
            text = profile?.program ?: "BS Information Technology",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569),
            maxLines = 1
          )
          Text(
            text = "${profile?.yearLevel ?: "3rd Year"} • ${profile?.section ?: "BSIT-301A"}",
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF64748B)
          )
        }
      }

      // Barcode & RFID Verification footer
      Column {
        // Barcode Canvas
        BarcodeCanvas(
          modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = "Active",
              tint = StiSuccess,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "OFFICIALLY ENROLLED • A.Y. 2026-2027",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = StiSuccess
            )
          }

          Text(
            text = "TERM 1",
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            color = StiPrimaryNavy
          )
        }
      }
    }
  }
}

@Composable
private fun IdCardBack(profile: StudentProfile?) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF9FAFD))
      .padding(16.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "STI STUDENT CARD • TERMS",
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = StiPrimaryNavy
          )
          Text(
            text = "RFID: ${profile?.rfidTag ?: "STI-GC-0284729"}",
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF64748B)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "This identification card is non-transferable and must be worn at all times while within STI campus premises. In case of loss, report immediately to the Registrar's Office.",
          fontSize = 9.sp,
          lineHeight = 12.sp,
          color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Emergency Contact Box
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFEEF3FA),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(8.dp)) {
            Text(
              text = "IN CASE OF EMERGENCY NOTIFY:",
              fontSize = 8.sp,
              fontWeight = FontWeight.Black,
              color = StiPrimaryNavy
            )
            Text(
              text = profile?.emergencyContact ?: "Elena M. Santos (Mother)",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF1E293B)
            )
            Text(
              text = profile?.emergencyPhone ?: "+63 918 555 3190",
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              color = StiPrimaryBlue
            )
          }
        }
      }

      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          Column {
            Text(
              text = "CAMPUS ADDRESS",
              fontSize = 8.sp,
              fontWeight = FontWeight.Bold,
              color = StiPrimaryNavy
            )
            Text(
              text = "University Parkway, BGC, Taguig City",
              fontSize = 9.sp,
              color = Color(0xFF64748B)
            )
            Text(
              text = "one.sti.edu • (02) 8888-7841",
              fontSize = 9.sp,
              color = StiPrimaryBlue
            )
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .width(100.dp)
                .height(1.dp)
                .background(Color(0xFF94A3B8))
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Student Signature",
              fontSize = 8.sp,
              color = Color(0xFF64748B)
            )
          }
        }
      }
    }
  }
}

@Composable
fun BarcodeCanvas(modifier: Modifier = Modifier) {
  Canvas(modifier = modifier) {
    val barWidths = floatArrayOf(
      3f, 1f, 4f, 2f, 1f, 3f, 5f, 2f, 1f, 4f, 2f, 1f, 5f, 2f, 3f, 1f, 2f, 4f, 1f, 3f,
      2f, 4f, 1f, 2f, 5f, 1f, 3f, 2f, 1f, 4f, 2f, 3f, 1f, 5f, 2f, 1f, 4f, 2f, 3f, 1f,
      4f, 2f, 1f, 3f, 5f, 2f, 1f, 4f, 2f, 1f, 3f, 2f, 4f, 1f, 2f, 3f, 1f, 4f, 2f, 1f
    )
    val totalUnits = barWidths.sum()
    val unitWidth = size.width / (totalUnits * 1.5f)
    var currentX = 0f

    for (i in barWidths.indices) {
      val w = barWidths[i] * unitWidth
      if (i % 2 == 0) {
        drawRect(
          color = Color(0xFF1E293B),
          topLeft = Offset(currentX, 0f),
          size = androidx.compose.ui.geometry.Size(w, size.height)
        )
      }
      currentX += w + (unitWidth * 0.5f)
    }
  }
}
