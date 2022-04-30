package com.example.schoolie.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.schoolie.R
import com.example.schoolie.adapters.LecturesAdapter
import com.example.schoolie.models.Course
import com.example.schoolie.models.Lecture
import com.example.schoolie.utilities.SavedPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_course_details.*
import kotlinx.android.synthetic.main.course_item.*

class CourseDetailsActivity : AppCompatActivity(), LecturesAdapter.SetClickListener {
    private lateinit var lecturesAdapter: LecturesAdapter
    private val coursesCollectionRef = Firebase.firestore.collection("courses")
    private val lecturesCollectionRef = Firebase.firestore.collection("lectures")

    var course_key: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)
        retrieveCourseDetails()

        course_key = intent.getStringExtra("course_key").toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveCourseDetails() {

        val uid = SavedPreferences.user_id

        if (uid != "") {
            coursesCollectionRef.get()
                .addOnCompleteListener {

                    val array = arrayListOf<Course>()
                    for (course in it.result.documents) {
                        val obj = course.toObject<Course>()
                        array.add(obj!!)
                        tv_course_title.text = obj.course_name
                        tv_course_desc.text = obj.course_desc
                        tv_lecturer_name.text = obj.course_instructor
                        Glide.with(applicationContext).load(obj.course_image).into(iv_course_icon)
                    }

                    Log.d("documents", "$array")

                    lecturesCollectionRef.whereEqualTo("course_key", course_key).get()
                        .addOnCompleteListener {
                            try {
                                val lectures = arrayListOf<Lecture>()
                                for (lecture in it.result.documents) {
                                    val docs = lecture.toObject<Lecture>()
                                    lectures.add(docs!!)
                                }
                                lecturesAdapter = LecturesAdapter(this, lectures)
                                lecturesAdapter.setListener(this)
                                rv_lectures.adapter = lecturesAdapter
                                lecturesAdapter.notifyDataSetChanged()
                            } catch (e: Exception) {
                                Toast.makeText(this, "$e", Toast.LENGTH_LONG).show()
                            }
                        }
                }
                .addOnFailureListener { exception ->
                    Log.d("---", "get failed with ", exception)
                }
        }
    }

    override fun onItemClickListener(position: Int, lecture: Lecture) {
        startActivity(Intent(this, LectureDetailsActivity::class.java))
    }
}
