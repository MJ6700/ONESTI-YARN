package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.ui.components.DigitalIdCard
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiGoldLight
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy
import com.example.ui.theme.StiSuccess
import kotlinx.coroutines.delay

@Composable
fun DigitalIdScreen(
  profile: StudentProfile?,
  onSimulateTap: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var securityToken by remember { mutableStateOf("STI-9841-BGC") }
  var countdown by remember { mutableIntStateOf(30) }
  var showScanSuccess by remember { mutableStateOf(false) }

  // Security Token auto-refresh countdown
  LaunchedEffect(countdown) {
    if (countdown > 0) {
      delay(1000)
      countdown--
    } else {
      countdown = 30
      securityToken = "STI-" + (1000..9999).random() + "-BGC"
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF4F7FB))
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Dynamic Anti-Counterfeit Security Token Header
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = Color(0xFFE2EAF4),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.VerifiedUser,
            contentDescription = null,
            tint = StiSuccess,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Live Anti-Counterfeit Token:",
            fontSize = 11.sp,
            color = Color(0xFF475569)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = securityToken,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = StiPrimaryNavy
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "(${countdown}s)",
            fontSize = 10.sp,
            color = Color(0xFF64748B)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Interactive Digital ID Card with Flip feature
    DigitalIdCard(
      profile = profile,
      onTapToScan = {}
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Gate Access Granted Banner Animation
    AnimatedVisibility(
      visible = showScanSuccess,
      enter = fadeIn(),
      exit = fadeOut()
    ) {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFD1E7DD),
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = StiSuccess,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "TURNSTILE UNLOCKED • ACCESS GRANTED",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color(0xFF0F5132)
            )
            Text(
              text = "Alexander M. Santos • Validated for Main Campus Entry",
              fontSize = 11.sp,
              color = Color(0xFF146C43)
            )
          }
        }
      }
    }

    // Turnstile Scan Simulation Action
    Button(
      onClick = {
        onSimulateTap("Main Entrance Turnstile #03", "ENTRY")
        showScanSuccess = true
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("scan_at_turnstile_button"),
      shape = RoundedCornerShape(14.dp),
      colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy)
    ) {
      Icon(
        imageVector = Icons.Default.Sensors,
        contentDescription = null,
        tint = StiAccentGold,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = "Tap At Campus Turnstile (NFC / RFID)",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color.White
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Secondary Actions: Library Barcode & Emergency Pass
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      OutlinedButton(
        onClick = {
          onSimulateTap("Library & Learning Commons", "ENTRY")
          showScanSuccess = true
        },
        modifier = Modifier
          .weight(1f)
          .testTag("digital_id_library_pass_button"),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.QrCode2,
          contentDescription = null,
          tint = StiPrimaryNavy,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Library Pass",
          fontSize = 12.sp,
          color = StiPrimaryNavy,
          fontWeight = FontWeight.Bold
        )
      }

      OutlinedButton(
        onClick = {
          onSimulateTap("East Gate Pedestrian", "EXIT")
          showScanSuccess = true
        },
        modifier = Modifier
          .weight(1f)
          .testTag("digital_id_gate_exit_button"),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Nfc,
          contentDescription = null,
          tint = StiPrimaryNavy,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Tap Gate Exit",
          fontSize = 12.sp,
          color = StiPrimaryNavy,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Guidelines Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "STI Digital ID Verification Rules",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = StiPrimaryNavy
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "• Required for entry at all STI College campus turnstiles, laboratories, and library checkout.\n• The dynamic security token updates every 30 seconds to prevent fraudulent reproduction.\n• For physical ID replacement, visit the Registrar's Office at 2nd Floor Admin Building.",
          fontSize = 11.sp,
          lineHeight = 16.sp,
          color = Color(0xFF64748B)
        )
      }
    }
  }
}
