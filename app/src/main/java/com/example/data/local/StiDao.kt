package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Announcement
import com.example.data.model.AttendanceRecord
import com.example.data.model.ClearanceItem
import com.example.data.model.CourseSchedule
import com.example.data.model.ElmsTask
import com.example.data.model.LedgerItem
import com.example.data.model.StudentProfile
import com.example.data.model.SubjectGrade
import kotlinx.coroutines.flow.Flow

@Dao
interface StiDao {
  // Student Profile
  @Query("SELECT * FROM student_profile LIMIT 1")
  fun getStudentProfile(): Flow<StudentProfile?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProfile(profile: StudentProfile)

  @Update
  suspend fun updateProfile(profile: StudentProfile)

  // Course Schedules
  @Query("SELECT * FROM course_schedules ORDER BY id ASC")
  fun getAllSchedules(): Flow<List<CourseSchedule>>

  @Query("SELECT * FROM course_schedules WHERE dayOfWeek = :day ORDER BY id ASC")
  fun getSchedulesByDay(day: String): Flow<List<CourseSchedule>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSchedules(schedules: List<CourseSchedule>)

  // Grades
  @Query("SELECT * FROM subject_grades WHERE term = :term ORDER BY id ASC")
  fun getGradesByTerm(term: String): Flow<List<SubjectGrade>>

  @Query("SELECT DISTINCT term FROM subject_grades")
  fun getAllTerms(): Flow<List<String>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGrades(grades: List<SubjectGrade>)

  @Update
  suspend fun updateGrade(grade: SubjectGrade)

  // Ledger / Tuition
  @Query("SELECT * FROM ledger_items ORDER BY id ASC")
  fun getLedgerItems(): Flow<List<LedgerItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLedgerItems(items: List<LedgerItem>)

  @Update
  suspend fun updateLedgerItem(item: LedgerItem)

  // ELMS Tasks
  @Query("SELECT * FROM elms_tasks ORDER BY isCompleted ASC, id ASC")
  fun getAllTasks(): Flow<List<ElmsTask>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTasks(tasks: List<ElmsTask>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTask(task: ElmsTask)

  @Update
  suspend fun updateTask(task: ElmsTask)

  // Attendance Records
  @Query("SELECT * FROM attendance_records ORDER BY timestamp DESC")
  fun getAttendanceRecords(): Flow<List<AttendanceRecord>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAttendanceRecord(record: AttendanceRecord)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAttendanceRecords(records: List<AttendanceRecord>)

  // Announcements
  @Query("SELECT * FROM announcements ORDER BY isPinned DESC, id DESC")
  fun getAnnouncements(): Flow<List<Announcement>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAnnouncements(announcements: List<Announcement>)

  @Query("UPDATE announcements SET isRead = 1 WHERE id = :id")
  suspend fun markAnnouncementAsRead(id: Long)

  // Clearance Items
  @Query("SELECT * FROM clearance_items ORDER BY id ASC")
  fun getClearanceItems(): Flow<List<ClearanceItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertClearanceItems(items: List<ClearanceItem>)
}
