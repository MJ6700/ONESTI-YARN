package com.example.data.local

import com.example.data.model.Announcement
import com.example.data.model.AttendanceRecord
import com.example.data.model.ClearanceItem
import com.example.data.model.CourseSchedule
import com.example.data.model.ElmsTask
import com.example.data.model.LedgerItem
import com.example.data.model.StudentProfile
import com.example.data.model.SubjectGrade

object DefaultData {
  val defaultProfile = StudentProfile(
    studentNumber = "02000284729",
    fullName = "ALEXANDER M. SANTOS",
    program = "Bachelor of Science in Information Technology",
    programCode = "BSIT",
    campus = "STI College Global City",
    yearLevel = "3rd Year",
    section = "BSIT-301A",
    email = "santos.284729@globalcity.sti.edu.ph",
    contactNumber = "+63 917 849 2011",
    gwa = 1.28,
    academicStanding = "President's Lister",
    totalUnitsEnrolled = 20,
    outstandingBalance = 9250.00,
    rfidTag = "STI-GC-0284729-A",
    enrollmentStatus = "Officially Enrolled",
    emergencyContact = "Elena M. Santos (Mother)",
    emergencyPhone = "+63 918 555 3190"
  )

  val defaultSchedules = listOf(
    CourseSchedule(
      id = 1,
      courseCode = "ITE312",
      courseTitle = "Mobile Application Development",
      room = "CompLab 402",
      instructor = "Prof. Ronald Vance Dela Cruz",
      instructorEmail = "ronald.delacruz@globalcity.sti.edu.ph",
      dayOfWeek = "Mon",
      startTime = "08:00 AM",
      endTime = "11:00 AM",
      units = 3.0,
      isLab = true,
      colorHex = 0xFF0B3C73
    ),
    CourseSchedule(
      id = 2,
      courseCode = "ITE310",
      courseTitle = "Information Assurance and Security 2",
      room = "CompLab 305",
      instructor = "Engr. Mark Anthony Flores",
      instructorEmail = "mark.flores@globalcity.sti.edu.ph",
      dayOfWeek = "Mon",
      startTime = "01:00 PM",
      endTime = "04:00 PM",
      units = 3.0,
      isLab = true,
      colorHex = 0xFF0A58CA
    ),
    CourseSchedule(
      id = 3,
      courseCode = "ITE314",
      courseTitle = "Systems Integration & Architecture",
      room = "Room 501 - West Wing",
      instructor = "Prof. Cynthia Bautista",
      instructorEmail = "cynthia.bautista@globalcity.sti.edu.ph",
      dayOfWeek = "Tue",
      startTime = "09:00 AM",
      endTime = "12:00 PM",
      units = 3.0,
      isLab = false,
      colorHex = 0xFF198754
    ),
    CourseSchedule(
      id = 4,
      courseCode = "GED105",
      courseTitle = "Purposive Communication",
      room = "Room 304",
      instructor = "Dr. Maria Luisa Reyes",
      instructorEmail = "maria.reyes@globalcity.sti.edu.ph",
      dayOfWeek = "Tue",
      startTime = "01:30 PM",
      endTime = "04:30 PM",
      units = 3.0,
      isLab = false,
      colorHex = 0xFF6F42C1
    ),
    CourseSchedule(
      id = 5,
      courseCode = "CS302",
      courseTitle = "Database Management Systems 2",
      room = "CompLab 401",
      instructor = "Prof. Jerome Villanueva",
      instructorEmail = "jerome.villanueva@globalcity.sti.edu.ph",
      dayOfWeek = "Wed",
      startTime = "08:30 AM",
      endTime = "11:30 AM",
      units = 3.0,
      isLab = true,
      colorHex = 0xFFD63384
    ),
    CourseSchedule(
      id = 6,
      courseCode = "ITE318",
      courseTitle = "Capstone Project and Research 1",
      room = "Innovation Hub 201",
      instructor = "Engr. Patrick Joseph Ramos",
      instructorEmail = "patrick.ramos@globalcity.sti.edu.ph",
      dayOfWeek = "Thu",
      startTime = "10:00 AM",
      endTime = "01:00 PM",
      units = 3.0,
      isLab = false,
      colorHex = 0xFFFD7E14
    ),
    CourseSchedule(
      id = 7,
      courseCode = "PE104",
      courseTitle = "Team Sports and Physical Wellness",
      room = "STI Gymnasium Court B",
      instructor = "Coach Gabriel Mendoza",
      instructorEmail = "gabriel.mendoza@globalcity.sti.edu.ph",
      dayOfWeek = "Fri",
      startTime = "08:00 AM",
      endTime = "10:00 AM",
      units = 2.0,
      isLab = false,
      colorHex = 0xFF20C997
    )
  )

  val defaultGrades = listOf(
    // 1st Term 2026-2027 (Current Term)
    SubjectGrade(
      id = 1,
      term = "1st Term 2026-2027",
      courseCode = "ITE312",
      courseTitle = "Mobile Application Development",
      units = 3.0,
      prelim = 1.25,
      midterm = 1.00,
      prefinal = 1.25,
      finalGrade = 1.15,
      remarks = "EXCELLENT"
    ),
    SubjectGrade(
      id = 2,
      term = "1st Term 2026-2027",
      courseCode = "ITE310",
      courseTitle = "Information Assurance and Security 2",
      units = 3.0,
      prelim = 1.50,
      midterm = 1.25,
      prefinal = 1.25,
      finalGrade = 1.30,
      remarks = "VERY GOOD"
    ),
    SubjectGrade(
      id = 3,
      term = "1st Term 2026-2027",
      courseCode = "ITE314",
      courseTitle = "Systems Integration & Architecture",
      units = 3.0,
      prelim = 1.25,
      midterm = 1.50,
      prefinal = 1.25,
      finalGrade = 1.33,
      remarks = "VERY GOOD"
    ),
    SubjectGrade(
      id = 4,
      term = "1st Term 2026-2027",
      courseCode = "CS302",
      courseTitle = "Database Management Systems 2",
      units = 3.0,
      prelim = 1.00,
      midterm = 1.00,
      prefinal = 1.25,
      finalGrade = 1.08,
      remarks = "EXCELLENT"
    ),
    SubjectGrade(
      id = 5,
      term = "1st Term 2026-2027",
      courseCode = "ITE318",
      courseTitle = "Capstone Project and Research 1",
      units = 3.0,
      prelim = 1.50,
      midterm = 1.25,
      prefinal = 1.50,
      finalGrade = 1.40,
      remarks = "VERY GOOD"
    ),
    SubjectGrade(
      id = 6,
      term = "1st Term 2026-2027",
      courseCode = "GED105",
      courseTitle = "Purposive Communication",
      units = 3.0,
      prelim = 1.25,
      midterm = 1.25,
      prefinal = 1.25,
      finalGrade = 1.25,
      remarks = "EXCELLENT"
    ),
    SubjectGrade(
      id = 7,
      term = "1st Term 2026-2027",
      courseCode = "PE104",
      courseTitle = "Team Sports and Physical Wellness",
      units = 2.0,
      prelim = 1.00,
      midterm = 1.00,
      prefinal = 1.00,
      finalGrade = 1.00,
      remarks = "EXCELLENT"
    ),
    // 2nd Term 2025-2026 (Previous Term)
    SubjectGrade(
      id = 8,
      term = "2nd Term 2025-2026",
      courseCode = "ITE208",
      courseTitle = "Data Structures and Algorithms",
      units = 3.0,
      prelim = 1.25,
      midterm = 1.25,
      prefinal = 1.25,
      finalGrade = 1.25,
      remarks = "EXCELLENT"
    ),
    SubjectGrade(
      id = 9,
      term = "2nd Term 2025-2026",
      courseCode = "ITE210",
      courseTitle = "Object-Oriented Programming (Java/Kotlin)",
      units = 3.0,
      prelim = 1.00,
      midterm = 1.25,
      prefinal = 1.00,
      finalGrade = 1.10,
      remarks = "EXCELLENT"
    ),
    SubjectGrade(
      id = 10,
      term = "2nd Term 2025-2026",
      courseCode = "MATH201",
      courseTitle = "Discrete Mathematics",
      units = 3.0,
      prelim = 1.50,
      midterm = 1.75,
      prefinal = 1.50,
      finalGrade = 1.55,
      remarks = "VERY GOOD"
    ),
    SubjectGrade(
      id = 11,
      term = "2nd Term 2025-2026",
      courseCode = "GED102",
      courseTitle = "Readings in Philippine History",
      units = 3.0,
      prelim = 1.25,
      midterm = 1.25,
      prefinal = 1.00,
      finalGrade = 1.15,
      remarks = "EXCELLENT"
    )
  )

  val defaultLedger = listOf(
    LedgerItem(
      id = 1,
      title = "Initial Down Payment & Enrollment",
      description = "Matriculation, Registration, and ID Validation",
      amount = 10000.00,
      dueDate = "Aug 15, 2026",
      status = "PAID",
      paidDate = "Aug 12, 2026",
      referenceNo = "OR-STI-2026-94812"
    ),
    LedgerItem(
      id = 2,
      title = "Prelim Period Installment",
      description = "Academic Tuition and IT Computer Laboratory Fees",
      amount = 9250.00,
      dueDate = "Sep 20, 2026",
      status = "PAID",
      paidDate = "Sep 18, 2026",
      referenceNo = "OR-STI-2026-95330"
    ),
    LedgerItem(
      id = 3,
      title = "Midterm Period Installment",
      description = "Tuition, Energy/Airconditioning and Campus Library Access",
      amount = 9250.00,
      dueDate = "Oct 25, 2026",
      status = "OUTSTANDING",
      paidDate = null,
      referenceNo = null
    ),
    LedgerItem(
      id = 4,
      title = "Finals Period Installment",
      description = "Final Assessment Balance and Semestral Clearance",
      amount = 10000.00,
      dueDate = "Dec 10, 2026",
      status = "UPCOMING",
      paidDate = null,
      referenceNo = null
    )
  )

  val defaultTasks = listOf(
    ElmsTask(
      id = 1,
      courseCode = "ITE312",
      title = "Laboratory 4: Compose Navigation and Jetpack Room",
      taskType = "LAB EXERCISE",
      deadline = "Tomorrow, 11:59 PM",
      isCompleted = false,
      score = null
    ),
    ElmsTask(
      id = 2,
      courseCode = "ITE310",
      title = "Case Study: Cryptography in Mobile Banking",
      taskType = "RESEARCH",
      deadline = "Oct 28, 2026",
      isCompleted = false,
      score = null
    ),
    ElmsTask(
      id = 3,
      courseCode = "CS302",
      title = "Quiz 3: Query Optimization and Stored Procedures",
      taskType = "QUIZ",
      deadline = "Oct 30, 2026",
      isCompleted = false,
      score = null
    ),
    ElmsTask(
      id = 4,
      courseCode = "ITE318",
      title = "Capstone Chapter 2: Review of Related Literature (RRL)",
      taskType = "PROJECT",
      deadline = "Nov 05, 2026",
      isCompleted = false,
      score = null
    ),
    ElmsTask(
      id = 5,
      courseCode = "ITE312",
      title = "Laboratory 3: Material 3 Theming & Responsive Layouts",
      taskType = "LAB EXERCISE",
      deadline = "Oct 12, 2026",
      isCompleted = true,
      score = "100/100",
      submissionDate = "Oct 11, 2026"
    ),
    ElmsTask(
      id = 6,
      courseCode = "GED105",
      title = "Speech Presentation: Technology & Ethical Dilemmas",
      taskType = "PROJECT",
      deadline = "Oct 08, 2026",
      isCompleted = true,
      score = "96/100",
      submissionDate = "Oct 07, 2026"
    )
  )

  val defaultAttendance = listOf(
    AttendanceRecord(
      id = 1,
      timestamp = System.currentTimeMillis() - 1000 * 60 * 45, // 45 mins ago
      dateFormatted = "Today, Oct 24",
      timeFormatted = "07:42 AM",
      gateLocation = "Main Campus Entrance - Turnstile #02",
      type = "ENTRY",
      status = "VERIFIED_RFID"
    ),
    AttendanceRecord(
      id = 2,
      timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24, // yesterday
      dateFormatted = "Yesterday, Oct 23",
      timeFormatted = "04:45 PM",
      gateLocation = "East Gate Pedestrian Exit",
      type = "EXIT",
      status = "VERIFIED_RFID"
    ),
    AttendanceRecord(
      id = 3,
      timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 28, // yesterday morning
      dateFormatted = "Yesterday, Oct 23",
      timeFormatted = "08:15 AM",
      gateLocation = "Main Campus Entrance - Turnstile #04",
      type = "ENTRY",
      status = "VERIFIED_RFID"
    ),
    AttendanceRecord(
      id = 4,
      timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 48,
      dateFormatted = "Oct 22, 2026",
      timeFormatted = "05:10 PM",
      gateLocation = "Library RFID Security Gate",
      type = "EXIT",
      status = "VERIFIED_RFID"
    ),
    AttendanceRecord(
      id = 5,
      timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 52,
      dateFormatted = "Oct 22, 2026",
      timeFormatted = "08:02 AM",
      gateLocation = "Main Campus Entrance - Turnstile #01",
      type = "ENTRY",
      status = "VERIFIED_RFID"
    )
  )

  val defaultAnnouncements = listOf(
    Announcement(
      id = 1,
      title = "Official Midterm Examination Schedule Released",
      category = "ACADEMICS",
      date = "Oct 22, 2026",
      content = "All undergraduate and senior high school midterm examinations will officially commence on November 3-7, 2026. Please ensure all student accounts are settled or examination permits are secured through ONE STI portal.",
      isPinned = true,
      isRead = false
    ),
    Announcement(
      id = 2,
      title = "STI National Youth Convention (NYC) 2026 Delegates Selection",
      category = "CAMPUS EVENT",
      date = "Oct 20, 2026",
      content = "Applications are now open for STI students eager to participate in this year's National Youth Convention featuring industry leaders from Google, Microsoft, and AWS. Submit portfolios through your program head.",
      isPinned = true,
      isRead = false
    ),
    Announcement(
      id = 3,
      title = "Campus High-Speed WiFi 6 Network Upgrade Complete",
      category = "ADVISORY",
      date = "Oct 18, 2026",
      content = "The IT Infrastructure department has successfully deployed WiFi 6 access points across all computer laboratories, library learning commons, and cafeteria floors. Connect using your ONE STI credentials.",
      isPinned = false,
      isRead = true
    ),
    Announcement(
      id = 4,
      title = "STI Career Expo & Partner Fair Announced",
      category = "CAREER",
      date = "Oct 15, 2026",
      content = "Over 60 global tech, business, and hospitality partner companies will conduct on-site interviews and internship hiring on November 18 at the Global City Auditorium.",
      isPinned = false,
      isRead = true
    )
  )

  val defaultClearance = listOf(
    ClearanceItem(
      id = 1,
      officeName = "University Library & Learning Commons",
      officerInCharge = "Ms. Grace Mendoza, Head Librarian",
      status = "CLEARED",
      remarks = "All borrowed books and research journals returned."
    ),
    ClearanceItem(
      id = 2,
      officeName = "Computer Laboratories & IT Dept",
      officerInCharge = "Engr. Ronald Vance Dela Cruz",
      status = "CLEARED",
      remarks = "Laboratory equipment, accounts and locker cleared."
    ),
    ClearanceItem(
      id = 3,
      officeName = "Guidance and Counseling Center",
      officerInCharge = "Dr. Maricel Santos",
      status = "CLEARED",
      remarks = "Annual student wellness evaluation completed."
    ),
    ClearanceItem(
      id = 4,
      officeName = "Accounting and Cashier Services",
      officerInCharge = "Mr. Jonathan Lopez, CPA",
      status = "PENDING",
      remarks = "Midterm installment balance pending settlement."
    ),
    ClearanceItem(
      id = 5,
      officeName = "Office of the College Registrar",
      officerInCharge = "Atty. Renato D. Cruz, Registrar",
      status = "CLEARED",
      remarks = "Complete admission credentials and NSO birth certificate on file."
    )
  )
}
