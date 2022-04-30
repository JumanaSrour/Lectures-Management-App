package com.example.schoolie.models

data class Lecture(
    var lecture_id: Int = 0,
    var lecture_title: String = "",
    var lecture_instructor: String = "",
    var lecture_image: String? = "",
    var lecture_desc: String = "",
    var course_key: String = "",
    var lecture_key: String = "",
    var lecture_video: String = "",
    var lecture_assignment: String = "",
)
