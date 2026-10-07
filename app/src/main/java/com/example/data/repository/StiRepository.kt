package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DefaultData
import com.example.data.local.StiDao
import com.example.data.model.Announcement
import com.example.data.model.AttendanceRecord
import com.example.data.model.ClearanceItem
import com.example.data.model.CourseSchedule
import com.example.data.model.ElmsTask
import com.example.data.model.LedgerItem
import com.example.data.model.StudentProfile
import com.example.data.model.SubjectGrade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StiRepository(private val dao: StiDao) {

  suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
    val existing = dao.getStudentProfile().firstOrNull()
    if (existing == null) {
      dao.insertProfile(DefaultData.defaultProfile)
      dao.insertSchedules(DefaultData.defaultSchedules)
      dao.insertGrades(DefaultData.defaultGrades)
      dao.insertLedgerItems(DefaultData.defaultLedger)
      dao.insertTasks(DefaultData.defaultTasks)
      dao.insertAttendanceRecords(DefaultData.defaultAttendance)
      dao.insertAnnouncements(DefaultData.defaultAnnouncements)
      dao.insertClearanceItems(DefaultData.defaultClearance)
    }
  }

  fun getStudentProfile(): Flow<StudentProfile?> = dao.getStudentProfile()
  suspend fun updateProfile(profile: StudentProfile) = dao.updateProfile(profile)

  fun getAllSchedules(): Flow<List<CourseSchedule>> = dao.getAllSchedules()
  fun getSchedulesByDay(day: String): Flow<List<CourseSchedule>> = dao.getSchedulesByDay(day)

  fun getGradesByTerm(term: String): Flow<List<SubjectGrade>> = dao.getGradesByTerm(term)
  fun getAllTerms(): Flow<List<String>> = dao.getAllTerms()
  suspend fun updateGrade(grade: SubjectGrade) = dao.updateGrade(grade)

  fun getLedgerItems(): Flow<List<LedgerItem>> = dao.getLedgerItems()
  suspend fun recordPayment(itemId: Long, amount: Double, method: String, reference: String) = withContext(Dispatchers.IO) {
    val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
    val paidDate = sdf.format(Date())
    // Update ledger item
    val items = dao.getLedgerItems().firstOrNull() ?: emptyList()
    val target = items.find { it.id == itemId }
    if (target != null) {
      val updated = target.copy(
        status = "PAID",
        paidDate = paidDate,
        referenceNo = reference
      )
      dao.updateLedgerItem(updated)

      // Also deduct from student outstanding balance
      val currentProfile = dao.getStudentProfile().firstOrNull()
      if (currentProfile != null) {
        val newBalance = (currentProfile.outstandingBalance - target.amount).coerceAtLeast(0.0)
        dao.updateProfile(currentProfile.copy(outstandingBalance = newBalance))
      }
    }
  }

  fun getAllTasks(): Flow<List<ElmsTask>> = dao.getAllTasks()
  suspend fun toggleTaskCompletion(task: ElmsTask) = withContext(Dispatchers.IO) {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val newStatus = !task.isCompleted
    val updated = task.copy(
      isCompleted = newStatus,
      submissionDate = if (newStatus) sdf.format(Date()) else null,
      score = if (newStatus && task.score == null) "Pending Grade" else task.score
    )
    dao.updateTask(updated)
  }

  suspend fun addNewTask(task: ElmsTask) = withContext(Dispatchers.IO) {
    dao.insertTask(task)
  }

  fun getAttendanceRecords(): Flow<List<AttendanceRecord>> = dao.getAttendanceRecords()
  suspend fun simulateRfidTap(gate: String, type: String) = withContext(Dispatchers.IO) {
    val now = System.currentTimeMillis()
    val dateSdf = SimpleDateFormat("Today, MMM dd", Locale.getDefault())
    val timeSdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val record = AttendanceRecord(
      timestamp = now,
      dateFormatted = dateSdf.format(Date(now)),
      timeFormatted = timeSdf.format(Date(now)),
      gateLocation = gate,
      type = type,
      status = "VERIFIED_RFID"
    )
    dao.insertAttendanceRecord(record)
  }

  fun getAnnouncements(): Flow<List<Announcement>> = dao.getAnnouncements()
  suspend fun markAnnouncementAsRead(id: Long) = dao.markAnnouncementAsRead(id)

  fun getClearanceItems(): Flow<List<ClearanceItem>> = dao.getClearanceItems()
}
