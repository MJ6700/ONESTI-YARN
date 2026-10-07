package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy

@Composable
fun StiHeader(
  profile: StudentProfile?,
  unreadAnnouncementsCount: Int,
  onNotificationsClick: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            StiPrimaryNavy,
            Color(0xFF072449)
          )
        )
      )
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // STI Logo and Portal Brand
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(StiAccentGold),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "STI",
              fontWeight = FontWeight.Black,
              color = StiPrimaryNavy,
              fontSize = 16.sp,
              letterSpacing = 0.5.sp
            )
          }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "ONE STI",
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              fontSize = 18.sp,
              letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = StiAccentGold
            ) {
              Text(
                text = "PORTAL",
                color = StiGoldDark,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }
          Text(
            text = profile?.campus ?: "STI College Global City",
            color = Color(0xFFBACFE8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Actions: Notification & Student ID Badge quick link
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onNotificationsClick,
          modifier = Modifier.testTag("header_notifications_button")
        ) {
          BadgedBox(
            badge = {
              if (unreadAnnouncementsCount > 0) {
                Badge(
                  containerColor = StiAccentGold,
                  contentColor = StiGoldDark
                ) {
                  Text(
                    text = unreadAnnouncementsCount.toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "Notifications",
              tint = Color.White
            )
          }
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Student Avatar pill
        Surface(
          shape = CircleShape,
          color = StiPrimaryBlue,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .clickable { onProfileClick() }
            .testTag("header_profile_button")
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(
              text = profile?.fullName?.split(" ")?.mapNotNull { it.firstOrNull() }?.take(2)?.joinToString("") ?: "AS",
              color = StiAccentGold,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }
      }
    }
  }
}
