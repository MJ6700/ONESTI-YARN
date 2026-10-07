package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profile")
data class StudentProfile(
  @PrimaryKey val studentNumber: String,
  val fullName: String,
  val program: String,
  val programCode: String,
  val campus: String,
  val yearLevel: String,
  val section: String,
  val email: String,
  val contactNumber: String,
  val gwa: Double,
  val academicStanding: String, // e.g. "President's Lister", "Dean's Lister", "Regular"
  val totalUnitsEnrolled: Int,
  val outstandingBalance: Double,
  val rfidTag: String,
  val enrollmentStatus: String, // "Officially Enrolled", "Pending Validation"
  val emergencyContact: String,
  val emergencyPhone: String
)

@Entity(tableName = "course_schedules")
data class CourseSchedule(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val courseCode: String,
  val courseTitle: String,
  val room: String,
  val instructor: String,
  val instructorEmail: String,
  val dayOfWeek: String, // "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
  val startTime: String, // "08:00 AM"
  val endTime: String,   // "10:30 AM"
  val units: Double,
  val isLab: Boolean,
  val colorHex: Long = 0xFF0B3C73
)

@Entity(tableName = "subject_grades")
data class SubjectGrade(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val term: String, // "1st Term 2026-2027", "2nd Term 2025-2026", "1st Term 2025-2026"
  val courseCode: String,
  val courseTitle: String,
  val units: Double,
  val prelim: Double,
  val midterm: Double,
  val prefinal: Double,
  val finalGrade: Double,
  val remarks: String // "PASSED", "ONGOING", "EXCELLENT"
)

@Entity(tableName = "ledger_items")
data class LedgerItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val description: String,
  val amount: Double,
  val dueDate: String,
  val status: String, // "PAID", "OUTSTANDING", "UPCOMING"
  val paidDate: String? = null,
  val referenceNo: String? = null
)

@Entity(tableName = "elms_tasks")
data class ElmsTask(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val courseCode: String,
  val title: String,
  val taskType: String, // "QUIZ", "LAB EXERCISE", "RESEARCH", "PROJECT"
  val deadline: String,
  val isCompleted: Boolean = false,
  val score: String? = null,
  val submissionDate: String? = null
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val timestamp: Long,
  val dateFormatted: String,
  val timeFormatted: String,
  val gateLocation: String,
  val type: String, // "ENTRY", "EXIT"
  val status: String = "VERIFIED_RFID"
)

@Entity(tableName = "announcements")
data class Announcement(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val category: String, // "ACADEMICS", "ADVISORY", "CAMPUS EVENT", "CAREER"
  val date: String,
  val content: String,
  val isPinned: Boolean = false,
  val isRead: Boolean = false
)

@Entity(tableName = "clearance_items")
data class ClearanceItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val officeName: String,
  val officerInCharge: String,
  val status: String, // "CLEARED", "PENDING", "HOLD"
  val remarks: String
)
