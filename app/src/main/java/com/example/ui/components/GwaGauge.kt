package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiGoldLight
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy

@Composable
fun GwaGauge(
  gwa: Double,
  standing: String,
  totalUnits: Int,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Circular Gauge Canvas
      Box(
        modifier = Modifier.size(90.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(80.dp)) {
          // In Philippine university system: 1.00 is highest (100%), 3.00 is passing (75%), 5.00 is failing
          // Map GWA 1.00..3.00 into a sweep percentage (1.00 = 100%, 3.00 = 30%)
          val normalized = ((3.0 - gwa.coerceIn(1.0, 3.0)) / 2.0).toFloat().coerceIn(0.2f, 1.0f)

          // Background track
          drawArc(
            color = Color(0xFFE2EAF4),
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
          )

          // Progress track
          drawArc(
            brush = Brush.sweepGradient(
              listOf(
                StiPrimaryNavy,
                StiPrimaryBlue,
                StiAccentGold
              )
            ),
            startAngle = 135f,
            sweepAngle = 270f * normalized,
            useCenter = false,
            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
          )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "%.2f".format(gwa),
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = StiPrimaryNavy
          )
          Text(
            text = "GWA",
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            color = Color(0xFF64748B)
          )
        }
      }

      Spacer(modifier = Modifier.width(16.dp))

      // Honor Standing & Units
      Column(modifier = Modifier.weight(1f)) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = StiGoldLight,
          modifier = Modifier.padding(bottom = 6.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = "Honor Standing",
              tint = StiGoldDark,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = standing.uppercase(),
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              color = StiGoldDark
            )
          }
        }

        Text(
          text = "Academic Status: Regular",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF334155)
        )
        Text(
          text = "Enrolled Units: $totalUnits Units (Current Term)",
          fontSize = 11.sp,
          color = Color(0xFF64748B)
        )
      }
    }
  }
}
