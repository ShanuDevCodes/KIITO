package com.kito.feature.attendance.presentation.components

import com.kito.feature.attendance.domain.model.Attendance

val sampleAttendanceEntities = listOf(
    Attendance(
        subjectCode = "00F4",
        subjectName = "Data Mining and Data Warehousing",
        attendedClasses = 4,
        totalClasses = 41,
        percentage = (4.0 / 41) * 100,
        facultyName = "Amiya Ranjan Panda"
    ),
    Attendance(
        subjectCode = "00F5",
        subjectName = "Engineering Economics",
        attendedClasses = 4,
        totalClasses = 39,
        percentage = (4.0 / 39) * 100,
        facultyName = "Arvind Kumar Yadav"
    ),
    Attendance(
        subjectCode = "00F6",
        subjectName = "Design and Analysis of Algorithms",
        attendedClasses = 1,
        totalClasses = 41,
        percentage = (1.0 / 41) * 100,
        facultyName = "Partha Sarathi Paul"
    ),
    Attendance(
        subjectCode = "00F7",
        subjectName = "Software Engineering",
        attendedClasses = 24,
        totalClasses = 52,
        percentage = (24.0 / 52) * 100,
        facultyName = "Ipsita Paul"
    ),
    Attendance(
        subjectCode = "00F8",
        subjectName = "Computer Networks",
        attendedClasses = 10,
        totalClasses = 40,
        percentage = (10.0 / 40) * 100,
        facultyName = "Nitin Varyani"
    ),
    Attendance(
        subjectCode = "00F9",
        subjectName = "Artificial Intelligence",
        attendedClasses = 18,
        totalClasses = 45,
        percentage = (18.0 / 45) * 100,
        facultyName = "Saswati Mishra"
    ),
    Attendance(
        subjectCode = "00G0",
        subjectName = "Compiler Design",
        attendedClasses = 12,
        totalClasses = 38,
        percentage = (12.0 / 38) * 100,
        facultyName = "Debasish Nayak"
    ),
    Attendance(
        subjectCode = "00G1",
        subjectName = "Cloud Computing",
        attendedClasses = 30,
        totalClasses = 48,
        percentage = (30.0 / 48) * 100,
        facultyName = "Rashmi Ranjan Behera"
    )
)
