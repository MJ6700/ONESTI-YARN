package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseSchedule
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiGoldLight
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy

@Composable
fun ScheduleScreen(
  schedules: List<CourseSchedule>,
  selectedDay: String,
  onSelectDay: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
  var selectedScheduleDetail by remember { mutableStateOf<CourseSchedule?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF4F7FB))
  ) {
    // Days of week selector
    Surface(
      color = Color.White,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(vertical = 12.dp)) {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(days) { day ->
            val isSelected = day.equals(selectedDay, ignoreCase = true)
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) StiPrimaryNavy else Color(0xFFF1F5F9),
              modifier = Modifier
                .clickable { onSelectDay(day) }
                .testTag("day_filter_$day")
            ) {
              Text(
                text = when (day) {
                  "Mon" -> "Monday"
                  "Tue" -> "Tuesday"
                  "Wed" -> "Wednesday"
                  "Thu" -> "Thursday"
                  "Fri" -> "Friday"
                  "Sat" -> "Saturday"
                  else -> day
                },
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) StiAccentGold else Color(0xFF475569),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
              )
            }
          }
        }
      }
    }

    // Schedule summary banner
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${schedules.size} Classes Scheduled",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = StiPrimaryNavy
      )
      val totalUnits = schedules.sumOf { it.units }
      Text(
        text = "Total: $totalUnits Units",
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF64748B)
      )
    }

    if (schedules.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.MeetingRoom,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(54.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No Classes for this Day",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = StiPrimaryNavy
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Use this time for ELMS coursework, group capstone, or personal study.",
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(schedules) { schedule ->
          CourseScheduleCard(
            schedule = schedule,
            onClick = { selectedScheduleDetail = schedule }
          )
        }
      }
    }
  }

  // Course Detail Dialog
  selectedScheduleDetail?.let { course ->
    AlertDialog(
      onDismissRequest = { selectedScheduleDetail = null },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = StiGoldLight,
            modifier = Modifier.padding(end = 8.dp)
          ) {
            Text(
              text = course.courseCode,
              fontWeight = FontWeight.Black,
              fontSize = 13.sp,
              color = StiGoldDark,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
          Text(
            text = "Course Details",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
        }
      },
      text = {
        Column {
          Text(
            text = course.courseTitle,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = StiPrimaryNavy
          )

          Spacer(modifier = Modifier.height(12.dp))

          DetailRow(icon = Icons.Default.AccessTime, label = "Time", value = "${course.startTime} - ${course.endTime}")
          DetailRow(icon = Icons.Default.LocationOn, label = "Room / Lab", value = course.room)
          DetailRow(icon = Icons.Default.Person, label = "Faculty", value = course.instructor)
          DetailRow(icon = Icons.Default.Email, label = "Email", value = course.instructorEmail)
          DetailRow(icon = Icons.Default.School, label = "Academic Credit", value = "${course.units} Units (${if (course.isLab) "Lecture + Lab" else "Lecture"})")

          Spacer(modifier = Modifier.height(12.dp))

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF1F5F9),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "STI Attendance Policy",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = StiPrimaryNavy
              )
              Text(
                text = "Maximum allowable absences for a 3-unit subject is 3 sessions (or 6 hours of lab). Beyond this, a student may be dropped from the course.",
                fontSize = 10.sp,
                color = Color(0xFF475569)
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { selectedScheduleDetail = null },
          colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy)
        ) {
          Text("Close")
        }
      }
    )
  }
}

@Composable
fun CourseScheduleCard(
  schedule: CourseSchedule,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("course_schedule_${schedule.courseCode}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(modifier = Modifier.fillMaxWidth()) {
      // Left vertical accent stripe
      Box(
        modifier = Modifier
          .width(6.dp)
          .height(115.dp)
          .background(Color(schedule.colorHex))
      )

      Column(
        modifier = Modifier
          .weight(1f)
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = schedule.courseCode,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 14.sp,
              color = StiPrimaryNavy
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (schedule.isLab) {
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFE2EAF4)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Computer,
                    contentDescription = "Lab",
                    tint = StiPrimaryNavy,
                    modifier = Modifier.size(10.dp)
                  )
                  Spacer(modifier = Modifier.width(3.dp))
                  Text(
                    text = "LAB",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = StiPrimaryNavy
                  )
                }
              }
            }
          }

          Text(
            text = "${schedule.units} Units",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B)
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = schedule.courseTitle,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          color = Color(0xFF1E293B),
          maxLines = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AccessTime,
              contentDescription = null,
              tint = StiPrimaryBlue,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${schedule.startTime} - ${schedule.endTime}",
              fontSize = 11.sp,
              color = Color(0xFF475569),
              fontWeight = FontWeight.Medium
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = Color(0xFF64748B),
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = schedule.room,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = StiPrimaryNavy
            )
          }
        }
      }
    }
  }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = StiPrimaryNavy,
      modifier = Modifier.size(16.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = "$label: ",
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      color = Color(0xFF64748B)
    )
    Text(
      text = value,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF1E293B)
    )
  }
}
