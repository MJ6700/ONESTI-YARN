package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Announcement
import com.example.data.model.AttendanceRecord
import com.example.data.model.ClearanceItem
import com.example.data.model.CourseSchedule
import com.example.data.model.ElmsTask
import com.example.data.model.LedgerItem
import com.example.data.model.StudentProfile
import com.example.data.model.SubjectGrade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    StudentProfile::class,
    CourseSchedule::class,
    SubjectGrade::class,
    LedgerItem::class,
    ElmsTask::class,
    AttendanceRecord::class,
    Announcement::class,
    ClearanceItem::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun stiDao(): StiDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "one_sti_database"
        ).addCallback(DatabaseCallback(scope))
         .fallbackToDestructiveMigration()
         .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialData(database.stiDao())
          }
        }
      }

      suspend fun populateInitialData(dao: StiDao) {
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
  }
}
