package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Announcement
import com.example.data.model.CourseSchedule
import com.example.data.model.StudentProfile
import com.example.ui.components.DigitalIdCard
import com.example.ui.components.GwaGauge
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiGoldLight
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy
import com.example.ui.theme.StiSuccess
import com.example.ui.theme.StiWarning
import com.example.ui.viewmodel.StiScreen

data class QuickActionItem(
  val label: String,
  val icon: ImageVector,
  val screen: StiScreen,
  val containerColor: Color,
  val contentColor: Color
)

@Composable
fun HomeScreen(
  profile: StudentProfile?,
  todaySchedules: List<CourseSchedule>,
  announcements: List<Announcement>,
  onNavigate: (StiScreen) -> Unit,
  onSimulateTap: (String, String) -> Unit,
  onAnnouncementClick: (Announcement) -> Unit,
  modifier: Modifier = Modifier
) {
  var showGateSimulatorDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF4F7FB)),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Welcome Greeting Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = StiPrimaryNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                  text = "Welcome back,",
                  fontSize = 12.sp,
                  color = StiAccentGold,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = profile?.fullName ?: "ALEXANDER SANTOS",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Black,
                  color = Color.White
                )
                Text(
                  text = "${profile?.programCode ?: "BSIT"} • ${profile?.section ?: "BSIT-301A"}",
                  fontSize = 12.sp,
                  color = Color(0xFFCFDCED)
                )
              }

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0x33FFFFFF),
                modifier = Modifier
                  .clickable { showGateSimulatorDialog = true }
                  .testTag("home_simulate_gate_button")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Sensors,
                    contentDescription = "Simulate Tap",
                    tint = StiAccentGold,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Tap Gate",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Stats Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              QuickStatItem(label = "Current Term", value = "1st Sem 2026")
              QuickStatItem(label = "Outstanding Bal", value = "₱%,.2f".format(profile?.outstandingBalance ?: 9250.0))
              QuickStatItem(label = "Status", value = "Enrolled", isSuccess = true)
            }
          }
        }
      }
    }

    // Up Next / Ongoing Class Banner
    item {
      val nextClass = todaySchedules.firstOrNull()
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigate(StiScreen.SCHEDULE) }
          .testTag("home_next_class_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                shape = CircleShape,
                color = Color(0xFFE8F5E9),
                modifier = Modifier.size(24.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(StiSuccess)
                  )
                }
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "HAPPENING TODAY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = StiPrimaryNavy,
                letterSpacing = 0.5.sp
              )
            }

            TextButton(
              onClick = { onNavigate(StiScreen.SCHEDULE) },
              modifier = Modifier.testTag("home_view_all_schedule_button")
            ) {
              Text(
                text = "View Timetable",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = StiPrimaryBlue
              )
              Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
            }
          }

          if (nextClass != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = StiGoldLight,
                modifier = Modifier.padding(end = 12.dp)
              ) {
                Text(
                  text = nextClass.courseCode,
                  fontWeight = FontWeight.Black,
                  fontSize = 13.sp,
                  color = StiGoldDark,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = nextClass.courseTitle,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = Color(0xFF1E293B),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Text(
                  text = "${nextClass.startTime} - ${nextClass.endTime} • ${nextClass.room}",
                  fontSize = 11.sp,
                  color = Color(0xFF64748B)
                )
              }
            }
          } else {
            Text(
              text = "No further classes scheduled for today. Enjoy your day!",
              fontSize = 12.sp,
              color = Color(0xFF64748B),
              modifier = Modifier.padding(vertical = 4.dp)
            )
          }
        }
      }
    }

    // Quick Portal Hub Grid (8 items)
    item {
      Text(
        text = "ONE STI Services",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = StiPrimaryNavy,
        modifier = Modifier.padding(vertical = 4.dp)
      )

      val actions = listOf(
        QuickActionItem("My Grades", Icons.Default.Grade, StiScreen.GRADES, Color(0xFFFFF3CD), StiGoldDark),
        QuickActionItem("Schedule", Icons.Default.CalendarMonth, StiScreen.SCHEDULE, Color(0xFFCFE2FF), StiPrimaryNavy),
        QuickActionItem("ONE ID", Icons.Default.Badge, StiScreen.DIGITAL_ID, Color(0xFFD1E7DD), StiSuccess),
        QuickActionItem("Ledger", Icons.Default.AccountBalanceWallet, StiScreen.LEDGER, Color(0xFFFFE5D0), StiWarning),
        QuickActionItem("ELMS Tasks", Icons.Default.Assignment, StiScreen.ELMS, Color(0xFFE2D9F3), Color(0xFF4A148C)),
        QuickActionItem("Gate Logs", Icons.Default.Sensors, StiScreen.ATTENDANCE, Color(0xFFD1ECF1), Color(0xFF0C5460)),
        QuickActionItem("Clearance", Icons.Default.FactCheck, StiScreen.SERVICES, Color(0xFFF8D7DA), Color(0xFF721C24)),
        QuickActionItem("Directory", Icons.Default.ContactPhone, StiScreen.SERVICES, Color(0xFFE2E3E5), Color(0xFF383D41))
      )

      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (i in actions.indices step 4) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            for (j in 0..3) {
              if (i + j < actions.size) {
                val item = actions[i + j]
                QuickActionCard(
                  item = item,
                  onClick = { onNavigate(item.screen) },
                  modifier = Modifier.weight(1f)
                )
                if (j < 3) Spacer(modifier = Modifier.width(8.dp))
              }
            }
          }
        }
      }
    }

    // GWA & Honors Gauge
    item {
      GwaGauge(
        gwa = profile?.gwa ?: 1.28,
        standing = profile?.academicStanding ?: "President's Lister",
        totalUnits = profile?.totalUnitsEnrolled ?: 20
      )
    }

    // Campus News & Advisories Section
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "STI Advisories & News",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = StiPrimaryNavy
        )
      }
    }

    items(announcements.take(3)) { announcement ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onAnnouncementClick(announcement) }
          .testTag("announcement_item_${announcement.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = when (announcement.category) {
                "ACADEMICS" -> Color(0xFFCFE2FF)
                "ADVISORY" -> Color(0xFFFFF3CD)
                else -> Color(0xFFD1E7DD)
              }
            ) {
              Text(
                text = announcement.category,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = when (announcement.category) {
                  "ACADEMICS" -> StiPrimaryNavy
                  "ADVISORY" -> StiGoldDark
                  else -> StiSuccess
                },
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            Text(
              text = announcement.date,
              fontSize = 11.sp,
              color = Color(0xFF94A3B8)
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = announcement.title,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFF1E293B)
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = announcement.content,
            fontSize = 11.sp,
            color = Color(0xFF64748B),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }

  // Turnstile Gate Simulator Dialog
  if (showGateSimulatorDialog) {
    AlertDialog(
      onDismissRequest = { showGateSimulatorDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Sensors,
            contentDescription = null,
            tint = StiPrimaryNavy,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Campus Gate RFID Scanner", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        Column {
          Text(
            text = "Select a campus turnstile to simulate tapping your STI student RFID card (${profile?.studentNumber ?: "02000284729"}):",
            fontSize = 13.sp,
            color = Color(0xFF475569)
          )
          Spacer(modifier = Modifier.height(14.dp))

          OutlinedButton(
            onClick = {
              onSimulateTap("Main Entrance Turnstile #02", "ENTRY")
              showGateSimulatorDialog = false
            },
            modifier = Modifier.fillMaxWidth().testTag("simulate_tap_main_entry")
          ) {
            Text("Main Entrance - Turnstile #02 (ENTRY)")
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = {
              onSimulateTap("Library RFID Security Gate", "ENTRY")
              showGateSimulatorDialog = false
            },
            modifier = Modifier.fillMaxWidth().testTag("simulate_tap_library_gate")
          ) {
            Text("Library & Learning Commons Gate")
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = {
              onSimulateTap("Main Campus Exit Turnstile #01", "EXIT")
              showGateSimulatorDialog = false
            },
            modifier = Modifier.fillMaxWidth().testTag("simulate_tap_main_exit")
          ) {
            Text("Main Campus Exit Turnstile #01 (EXIT)")
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showGateSimulatorDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
fun QuickStatItem(label: String, value: String, isSuccess: Boolean = false) {
  Column {
    Text(
      text = label,
      fontSize = 10.sp,
      color = Color(0xFFBACFE8)
    )
    Text(
      text = value,
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      color = if (isSuccess) StiAccentGold else Color.White
    )
  }
}

@Composable
fun QuickActionCard(
  item: QuickActionItem,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clickable { onClick() }
      .testTag("quick_action_${item.label.lowercase().replace(" ", "_")}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(item.containerColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = item.icon,
          contentDescription = item.label,
          tint = item.contentColor,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = item.label,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = StiPrimaryNavy,
        maxLines = 1
      )
    }
  }
}
