package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy
import com.example.ui.theme.StiSuccess
import com.example.ui.theme.StiWarning

@Composable
fun AttendanceScreen(
  records: List<AttendanceRecord>,
  onSimulateTap: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF4F7FB)),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Attendance Stats Summary Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = StiPrimaryNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              brush = Brush.horizontalGradient(
                colors = listOf(
                  StiPrimaryNavy,
                  StiPrimaryBlue
                )
              )
            )
            .padding(18.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "CLASS ATTENDANCE RATE",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = StiAccentGold,
                  letterSpacing = 1.sp
                )
                Text(
                  text = "96.4%",
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Black,
                  color = Color.White
                )
              }

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = StiAccentGold
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = StiGoldDark,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "EXCELLENT",
                    color = StiGoldDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
              progress = { 0.964f },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = StiAccentGold,
              trackColor = Color(0x33FFFFFF)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Total Sessions Attended: 54 / 56 • 0 Unexcused Absences",
              fontSize = 11.sp,
              color = Color(0xFFBACFE8)
            )
          }
        }
      }
    }

    // Gate RFID Logs Header & Quick Action
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Campus Turnstile & Gate Logs",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = StiPrimaryNavy
        )

        Button(
          onClick = {
            onSimulateTap("Main Entrance Turnstile #02", "ENTRY")
          },
          colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("attendance_quick_tap_button")
        ) {
          Icon(
            imageVector = Icons.Default.Sensors,
            contentDescription = null,
            tint = StiAccentGold,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Tap Gate", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    // Timeline of Gate Logs
    items(records) { log ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("attendance_log_${log.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(
                if (log.type == "ENTRY") Color(0xFFD1E7DD) else Color(0xFFFFE5D0)
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (log.type == "ENTRY") Icons.Default.Login else Icons.Default.ExitToApp,
              contentDescription = log.type,
              tint = if (log.type == "ENTRY") StiSuccess else StiWarning,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (log.type == "ENTRY") "Campus Tap-In (ENTRY)" else "Campus Tap-Out (EXIT)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF1E293B)
              )

              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFF1F5F9)
              ) {
                Text(
                  text = "VERIFIED RFID",
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold,
                  color = StiPrimaryNavy,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
              text = log.gateLocation,
              fontSize = 11.sp,
              color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "${log.dateFormatted} • ${log.timeFormatted}",
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              color = StiPrimaryBlue,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}
