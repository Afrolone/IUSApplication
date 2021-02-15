package com.afrolone.iusapp.models

import com.afrolone.iusapp.models.Grade

data class Course(
    val courseCode: String,
    val courseName: String,
    val branch: String,
    val staff: String,
    val attendanceStatus: String,
    val grades: ArrayList<Grade>,
    val yid: String,
    val courseFinalGrade: String,
    val finalGrade: String,
    val makeupGrade: String,
    val letterGrade: String,
    val classAverage: String,
    val semesterName: String
)