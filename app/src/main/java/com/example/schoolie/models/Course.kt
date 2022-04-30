package com.example.schoolie.models

data class Course(
    var course_id: Int = 0,
    var course_name: String = "",
    var course_instructor: String = "",
    var course_desc: String = "",
    var course_image: String? = "",
    var course_key: String = "",
    var student_ids: String = ""
)
