package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Announcement
import com.example.data.model.AttendanceRecord
import com.example.data.model.ClearanceItem
import com.example.data.model.CourseSchedule
import com.example.data.model.ElmsTask
import com.example.data.model.LedgerItem
import com.example.data.model.StudentProfile
import com.example.data.model.SubjectGrade
import com.example.data.repository.StiRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

enum class StiScreen(val label: String) {
  HOME("Home"),
  SCHEDULE("Schedule"),
  GRADES("Grades"),
  DIGITAL_ID("ONE ID"),
  LEDGER("Ledger"),
  ELMS("eLMS"),
  ATTENDANCE("Turnstile"),
  SERVICES("Services"),
  WEB_PORTAL("Online Portal")
}

data class UiMessage(val message: String, val isSuccess: Boolean = true)

class StiViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: StiRepository

  private val _currentScreen = MutableStateFlow(StiScreen.HOME)
  val currentScreen: StateFlow<StiScreen> = _currentScreen.asStateFlow()

  private val _currentWebUrl = MutableStateFlow("https://one.sti.edu")
  val currentWebUrl: StateFlow<String> = _currentWebUrl.asStateFlow()

  private val _selectedDay = MutableStateFlow("Mon")
  val selectedDay: StateFlow<String> = _selectedDay.asStateFlow()

  private val _selectedTerm = MutableStateFlow("1st Term 2026-2027")
  val selectedTerm: StateFlow<String> = _selectedTerm.asStateFlow()

  private val _eventFlow = MutableSharedFlow<UiMessage>()
  val eventFlow: SharedFlow<UiMessage> = _eventFlow.asSharedFlow()

  // Selected ledger item for payment modal
  private val _paymentModalItem = MutableStateFlow<LedgerItem?>(null)
  val paymentModalItem: StateFlow<LedgerItem?> = _paymentModalItem.asStateFlow()

  // Selected announcement for detail modal
  private val _selectedAnnouncement = MutableStateFlow<Announcement?>(null)
  val selectedAnnouncement: StateFlow<Announcement?> = _selectedAnnouncement.asStateFlow()

  // Task filter (ALL, PENDING, COMPLETED)
  private val _taskFilter = MutableStateFlow("ALL")
  val taskFilter: StateFlow<String> = _taskFilter.asStateFlow()

  init {
    val database = AppDatabase.getDatabase(application, viewModelScope)
    repository = StiRepository(database.stiDao())

    // Determine today's day of week
    val calendar = Calendar.getInstance()
    val dayStr = when (calendar.get(Calendar.DAY_OF_WEEK)) {
      Calendar.MONDAY -> "Mon"
      Calendar.TUESDAY -> "Tue"
      Calendar.WEDNESDAY -> "Wed"
      Calendar.THURSDAY -> "Thu"
      Calendar.FRIDAY -> "Fri"
      Calendar.SATURDAY -> "Sat"
      else -> "Mon"
    }
    _selectedDay.value = dayStr

    viewModelScope.launch {
      repository.ensureInitialized()
    }
  }

  val profile: StateFlow<StudentProfile?> = repository.getStudentProfile()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val allSchedules: StateFlow<List<CourseSchedule>> = repository.getAllSchedules()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val daySchedules: StateFlow<List<CourseSchedule>> = combine(allSchedules, _selectedDay) { schedules, day ->
    schedules.filter { it.dayOfWeek.equals(day, ignoreCase = true) }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allTerms: StateFlow<List<String>> = repository.getAllTerms()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("1st Term 2026-2027", "2nd Term 2025-2026"))

  val currentTermGrades: StateFlow<List<SubjectGrade>> = repository.getGradesByTerm("1st Term 2026-2027")
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val selectedTermGrades: StateFlow<List<SubjectGrade>> = combine(
    _selectedTerm,
    repository.getAllTerms()
  ) { term, _ -> term }
    .combine(repository.getGradesByTerm(_selectedTerm.value)) { _, grades -> grades }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Dynamic GWA calculation for current grades
  val calculatedGwa: StateFlow<Double> = currentTermGrades.combine(_selectedTerm) { grades, _ ->
    if (grades.isEmpty()) 0.0
    else {
      var totalUnits = 0.0
      var weightedSum = 0.0
      for (g in grades) {
        val gradeVal = if (g.finalGrade > 0) g.finalGrade else (g.prelim + g.midterm + g.prefinal) / 3.0
        weightedSum += gradeVal * g.units
        totalUnits += g.units
      }
      if (totalUnits > 0) Math.round((weightedSum / totalUnits) * 100.0) / 100.0 else 0.0
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.28)

  val ledgerItems: StateFlow<List<LedgerItem>> = repository.getLedgerItems()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allTasks: StateFlow<List<ElmsTask>> = repository.getAllTasks()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val filteredTasks: StateFlow<List<ElmsTask>> = combine(allTasks, _taskFilter) { tasks, filter ->
    when (filter) {
      "PENDING" -> tasks.filter { !it.isCompleted }
      "COMPLETED" -> tasks.filter { it.isCompleted }
      else -> tasks
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val attendanceRecords: StateFlow<List<AttendanceRecord>> = repository.getAttendanceRecords()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val announcements: StateFlow<List<Announcement>> = repository.getAnnouncements()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val clearanceItems: StateFlow<List<ClearanceItem>> = repository.getClearanceItems()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun navigateTo(screen: StiScreen) {
    _currentScreen.value = screen
  }

  fun setWebUrl(url: String) {
    val trimmed = url.trim()
    val formatted = if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
      "https://$trimmed"
    } else {
      trimmed
    }
    _currentWebUrl.value = formatted
    _currentScreen.value = StiScreen.WEB_PORTAL
  }

  fun selectDay(day: String) {
    _selectedDay.value = day
  }

  fun selectTerm(term: String) {
    _selectedTerm.value = term
  }

  fun setTaskFilter(filter: String) {
    _taskFilter.value = filter
  }

  fun openPaymentModal(item: LedgerItem) {
    _paymentModalItem.value = item
  }

  fun closePaymentModal() {
    _paymentModalItem.value = null
  }

  fun selectAnnouncement(announcement: Announcement?) {
    _selectedAnnouncement.value = announcement
    if (announcement != null && !announcement.isRead) {
      viewModelScope.launch {
        repository.markAnnouncementAsRead(announcement.id)
      }
    }
  }

  fun submitPayment(itemId: Long, method: String) {
    viewModelScope.launch {
      val ref = "STI-" + UUID.randomUUID().toString().substring(0, 8).uppercase()
      val item = _paymentModalItem.value
      val amount = item?.amount ?: 0.0
      repository.recordPayment(itemId, amount, method, ref)
      _paymentModalItem.value = null
      _eventFlow.emit(UiMessage("Payment of ₱%,.2f via $method successful! Ref: $ref".format(amount), true))
    }
  }

  fun toggleTask(task: ElmsTask) {
    viewModelScope.launch {
      repository.toggleTaskCompletion(task)
      val action = if (!task.isCompleted) "completed and submitted" else "marked pending"
      _eventFlow.emit(UiMessage("ELMS Task \"${task.title}\" $action!", true))
    }
  }

  fun addNewTask(code: String, title: String, type: String, deadline: String) {
    viewModelScope.launch {
      val task = ElmsTask(
        courseCode = code.ifBlank { "GEN100" },
        title = title,
        taskType = type,
        deadline = deadline.ifBlank { "Next Week" },
        isCompleted = false
      )
      repository.addNewTask(task)
      _eventFlow.emit(UiMessage("New assignment \"$title\" added to ELMS tracker!", true))
    }
  }

  fun simulateTurnstileScan(gate: String, type: String) {
    viewModelScope.launch {
      repository.simulateRfidTap(gate, type)
      val action = if (type == "ENTRY") "Campus Access Granted! Welcome to STI." else "Exit Logged. Safe travels!"
      _eventFlow.emit(UiMessage("RFID Verified at $gate: $action", true))
    }
  }

  fun updateGradeValue(grade: SubjectGrade, prelim: Double, midterm: Double, prefinal: Double, finalG: Double) {
    viewModelScope.launch {
      val remarks = when {
        finalG in 1.0..1.25 -> "EXCELLENT"
        finalG in 1.26..1.75 -> "VERY GOOD"
        finalG in 1.76..2.50 -> "GOOD"
        finalG in 2.51..3.00 -> "PASSED"
        else -> "FAILED"
      }
      val updated = grade.copy(
        prelim = prelim,
        midterm = midterm,
        prefinal = prefinal,
        finalGrade = finalG,
        remarks = remarks
      )
      repository.updateGrade(updated)
      _eventFlow.emit(UiMessage("Updated grade for ${grade.courseCode} to $finalG ($remarks)", true))
    }
  }
}
