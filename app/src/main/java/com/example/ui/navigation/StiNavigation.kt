package com.example.ui.navigation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Announcement
import com.example.ui.components.StiHeader
import com.example.ui.screens.AttendanceScreen
import com.example.ui.screens.DigitalIdScreen
import com.example.ui.screens.ElmsTasksScreen
import com.example.ui.screens.GradesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LedgerScreen
import com.example.ui.screens.ScheduleScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.WebPortalScreen
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy
import com.example.ui.viewmodel.StiScreen
import com.example.ui.viewmodel.StiViewModel
import kotlinx.coroutines.flow.collectLatest

data class NavItem(
  val screen: StiScreen,
  val icon: ImageVector,
  val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StiAppNavHost(
  viewModel: StiViewModel,
  modifier: Modifier = Modifier
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val currentWebUrl by viewModel.currentWebUrl.collectAsState()
  val profile by viewModel.profile.collectAsState()
  val todaySchedules by viewModel.daySchedules.collectAsState()
  val selectedDay by viewModel.selectedDay.collectAsState()
  val allTerms by viewModel.allTerms.collectAsState()
  val selectedTerm by viewModel.selectedTerm.collectAsState()
  val selectedTermGrades by viewModel.selectedTermGrades.collectAsState()
  val ledgerItems by viewModel.ledgerItems.collectAsState()
  val paymentModalItem by viewModel.paymentModalItem.collectAsState()
  val filteredTasks by viewModel.filteredTasks.collectAsState()
  val taskFilter by viewModel.taskFilter.collectAsState()
  val attendanceRecords by viewModel.attendanceRecords.collectAsState()
  val announcements by viewModel.announcements.collectAsState()
  val clearanceItems by viewModel.clearanceItems.collectAsState()
  val selectedAnnouncement by viewModel.selectedAnnouncement.collectAsState()

  val unreadAnnouncementsCount = announcements.count { !it.isRead }
  val snackbarHostState = remember { SnackbarHostState() }
  var showNotificationsModal by remember { mutableStateOf(false) }

  // Collect Event messages for toast/snackbar
  LaunchedEffect(Unit) {
    viewModel.eventFlow.collectLatest { msg ->
      snackbarHostState.showSnackbar(msg.message)
    }
  }

  val navItems = listOf(
    NavItem(StiScreen.HOME, Icons.Default.Home, "Home"),
    NavItem(StiScreen.SCHEDULE, Icons.Default.CalendarMonth, "Schedule"),
    NavItem(StiScreen.GRADES, Icons.Default.Grade, "Grades"),
    NavItem(StiScreen.DIGITAL_ID, Icons.Default.Badge, "ONE ID"),
    NavItem(StiScreen.LEDGER, Icons.Default.AccountBalanceWallet, "Ledger")
  )

  Scaffold(
    modifier = modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    topBar = {
      if (currentScreen == StiScreen.WEB_PORTAL) {
        // WebPortalScreen has its own header
      } else if (currentScreen == StiScreen.HOME) {
        StiHeader(
          profile = profile,
          unreadAnnouncementsCount = unreadAnnouncementsCount,
          onNotificationsClick = { showNotificationsModal = true },
          onProfileClick = { viewModel.navigateTo(StiScreen.DIGITAL_ID) }
        )
      } else {
        TopAppBar(
          title = {
            Text(
              text = when (currentScreen) {
                StiScreen.SCHEDULE -> "Academic Schedule"
                StiScreen.GRADES -> "Grades & Evaluation"
                StiScreen.DIGITAL_ID -> "STI Digital ID Pass"
                StiScreen.LEDGER -> "Tuition & Accounts"
                StiScreen.ELMS -> "STI eLMS Coursework"
                StiScreen.ATTENDANCE -> "Campus Turnstile Logs"
                StiScreen.SERVICES -> "Services & Clearance"
                else -> "ONE STI Portal"
              },
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = Color.White
            )
          },
          navigationIcon = {
            if (currentScreen in listOf(StiScreen.ELMS, StiScreen.ATTENDANCE, StiScreen.SERVICES)) {
              IconButton(
                onClick = { viewModel.navigateTo(StiScreen.HOME) },
                modifier = Modifier.testTag("nav_back_button")
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
                  tint = Color.White
                )
              }
            }
          },
          actions = {
            IconButton(
              onClick = { showNotificationsModal = true },
              modifier = Modifier.testTag("appbar_notifications_button")
            ) {
              BadgedBox(
                badge = {
                  if (unreadAnnouncementsCount > 0) {
                    Badge(
                      containerColor = StiAccentGold,
                      contentColor = StiGoldDark
                    ) {
                      Text(unreadAnnouncementsCount.toString())
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
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = StiPrimaryNavy
          )
        )
      }
    },
    bottomBar = {
      NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp
      ) {
        navItems.forEach { item ->
          val isSelected = currentScreen == item.screen
          NavigationBarItem(
            selected = isSelected,
            onClick = { viewModel.navigateTo(item.screen) },
            icon = {
              Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                modifier = Modifier.size(22.dp)
              )
            },
            label = {
              Text(
                text = item.label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = StiPrimaryNavy,
              selectedTextColor = StiPrimaryNavy,
              indicatorColor = StiAccentGold,
              unselectedIconColor = Color(0xFF64748B),
              unselectedTextColor = Color(0xFF64748B)
            ),
            modifier = Modifier.testTag("nav_tab_${item.label.lowercase().replace(" ", "_")}")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentScreen) {
        StiScreen.WEB_PORTAL -> {
          WebPortalScreen(
            targetUrl = currentWebUrl,
            onUrlChange = { viewModel.setWebUrl(it) },
            onSwitchToNativeHub = { viewModel.navigateTo(StiScreen.HOME) }
          )
        }
        StiScreen.HOME -> {
          HomeScreen(
            profile = profile,
            todaySchedules = todaySchedules,
            announcements = announcements,
            onNavigate = { viewModel.navigateTo(it) },
            onSimulateTap = { gate, type -> viewModel.simulateTurnstileScan(gate, type) },
            onAnnouncementClick = { viewModel.selectAnnouncement(it) }
          )
        }
        StiScreen.SCHEDULE -> {
          ScheduleScreen(
            schedules = todaySchedules,
            selectedDay = selectedDay,
            onSelectDay = { viewModel.selectDay(it) }
          )
        }
        StiScreen.GRADES -> {
          GradesScreen(
            grades = selectedTermGrades,
            selectedTerm = selectedTerm,
            allTerms = allTerms,
            onSelectTerm = { viewModel.selectTerm(it) },
            onUpdateGrade = { g, p, m, pf, f -> viewModel.updateGradeValue(g, p, m, pf, f) }
          )
        }
        StiScreen.DIGITAL_ID -> {
          DigitalIdScreen(
            profile = profile,
            onSimulateTap = { gate, type -> viewModel.simulateTurnstileScan(gate, type) }
          )
        }
        StiScreen.LEDGER -> {
          LedgerScreen(
            profile = profile,
            ledgerItems = ledgerItems,
            selectedPaymentItem = paymentModalItem,
            onOpenPaymentModal = { viewModel.openPaymentModal(it) },
            onClosePaymentModal = { viewModel.closePaymentModal() },
            onSubmitPayment = { id, method -> viewModel.submitPayment(id, method) }
          )
        }
        StiScreen.ELMS -> {
          ElmsTasksScreen(
            tasks = filteredTasks,
            currentFilter = taskFilter,
            onFilterChange = { viewModel.setTaskFilter(it) },
            onToggleTask = { viewModel.toggleTask(it) },
            onAddNewTask = { code, title, type, dl -> viewModel.addNewTask(code, title, type, dl) }
          )
        }
        StiScreen.ATTENDANCE -> {
          AttendanceScreen(
            records = attendanceRecords,
            onSimulateTap = { gate, type -> viewModel.simulateTurnstileScan(gate, type) }
          )
        }
        StiScreen.SERVICES -> {
          ServicesScreen(
            clearanceItems = clearanceItems
          )
        }
      }
    }
  }

  // Announcement Detail Dialog
  selectedAnnouncement?.let { announcement ->
    AlertDialog(
      onDismissRequest = { viewModel.selectAnnouncement(null) },
      title = {
        Column {
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = Color(0xFFCFE2FF),
            modifier = Modifier.padding(bottom = 6.dp)
          ) {
            Text(
              text = announcement.category,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = StiPrimaryNavy,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
          Text(text = announcement.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        Column {
          Text(text = "Date Posted: ${announcement.date}", fontSize = 11.sp, color = Color(0xFF94A3B8))
          Spacer(modifier = Modifier.height(10.dp))
          Text(text = announcement.content, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF334155))
        }
      },
      confirmButton = {
        Button(
          onClick = { viewModel.selectAnnouncement(null) },
          colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy)
        ) {
          Text("Close")
        }
      }
    )
  }

  // Notifications Modal
  if (showNotificationsModal) {
    AlertDialog(
      onDismissRequest = { showNotificationsModal = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = StiPrimaryNavy, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Campus Notifications", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          announcements.forEach { notice ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (notice.isRead) Color(0xFFF8FAFC) else Color(0xFFEFF6FF),
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  viewModel.selectAnnouncement(notice)
                  showNotificationsModal = false
                }
                .padding(4.dp)
            ) {
              Column(modifier = Modifier.padding(8.dp)) {
                Text(text = notice.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StiPrimaryNavy)
                Text(text = notice.date, fontSize = 10.sp, color = Color(0xFF64748B))
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showNotificationsModal = false }) {
          Text("Close")
        }
      }
    )
  }
}
