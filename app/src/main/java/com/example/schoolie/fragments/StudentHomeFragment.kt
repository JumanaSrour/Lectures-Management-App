package com.example.schoolie.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schoolie.R
import com.example.schoolie.activities.CourseDetailsActivity
import com.example.schoolie.adapters.CoursesAdapter
import com.example.schoolie.models.Course
import com.example.schoolie.utilities.SavedPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home_student.*
import kotlinx.android.synthetic.main.fragment_student_courses.*

class HomeFragment : Fragment(), CoursesAdapter.SetClickListener {
    private lateinit var coursesAdapter: CoursesAdapter
    private val coursesCollectionRef = Firebase.firestore.collection("courses")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrieveRegisteredCourses()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveRegisteredCourses() {
        val uid = SavedPreferences.user_id

        if (uid != "") {
            // only show registered courses
            coursesCollectionRef.get()
                .addOnCompleteListener {
                    val array = arrayListOf<Course>()
                    for (course in it.result.documents) {
                        val obj = course.toObject<Course>()
                        array.add(obj!!)
                    }
                    Log.d("documents", "$array")

                    coursesAdapter = CoursesAdapter(requireContext(), array)
                    coursesAdapter.setListener(this)
                    rv_home_std.adapter = coursesAdapter
                    coursesAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.d("---", "get failed with ", exception)
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_student, container, false)
    }

    override fun onItemClickListener(position: Int, course: Course) {
        val intent = Intent(activity, CourseDetailsActivity::class.java)
        intent.putExtra("course_key", course.course_key)
        activity?.startActivity(intent)
    }

    override fun onButtonClickListener(position: Int, course: Course) {
        TODO("Not yet implemented")
    }
}
