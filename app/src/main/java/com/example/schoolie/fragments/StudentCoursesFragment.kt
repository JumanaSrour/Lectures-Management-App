package com.example.schoolie.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.schoolie.R
import com.example.schoolie.activities.CourseDetailsActivity
import com.example.schoolie.adapters.CoursesAdapter
import com.example.schoolie.manager.FcmNotificationBuilder
import com.example.schoolie.models.Course
import com.example.schoolie.models.CustomAddCourseDialog
import com.example.schoolie.utilities.SavedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.course_item.*
import kotlinx.android.synthetic.main.course_item.view.*
import kotlinx.android.synthetic.main.fragment_home_student.*
import kotlinx.android.synthetic.main.fragment_student_courses.*
import kotlinx.android.synthetic.main.fragment_student_courses.view.*

var course_key = ""
class StudentCoursesFragment : Fragment(), CoursesAdapter.SetClickListener {
    private lateinit var coursesAdapter: CoursesAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var courseName: String
    private lateinit var courseImage: ImageView
    private lateinit var courseInstructor: String
    private val coursesCollectionRef = Firebase.firestore.collection("courses")

    private var myCourseCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_courses, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCount()
    }

    private fun getCount() {
        val uid = SavedPreferences.user_id
        coursesCollectionRef.whereArrayContains("student_ids", uid as Any).get()
            .addOnCompleteListener {

                myCourseCount = it.result.documents.count()

                retrieveCourses()
            }
            .addOnFailureListener { exception ->
                Log.d("---", "get failed with ", exception)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveCourses() {
        val uid = SavedPreferences.user_id

        if (uid != "") {
            coursesCollectionRef.get()
                .addOnCompleteListener {
                    val array = arrayListOf<Course>()
                    for (course in it.result.documents) {
                        val obj = course.toObject<Course>()
                        course_key = obj!!.course_key
                        array.add(obj)
                    }
                    Log.d("myCourseCount", "$myCourseCount")

                    coursesAdapter = CoursesAdapter(requireContext(), myCourseCount, array)
                    coursesAdapter.setListener(this)
                    rv_courses_std.adapter = coursesAdapter

                    coursesAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.d("---", "get failed with ", exception)
                }
        }
    }

    override fun onItemClickListener(position: Int, course: Course) {
        val intent = Intent(activity, CourseDetailsActivity::class.java)
        intent.putExtra("course_key", course.course_key)
        Log.d("course_key", "onButtonClickListener:${course.course_key} ")
        activity?.startActivity(intent)
    }

    override fun onButtonClickListener(position: Int, course: Course) {
        createAddCourseDialog(course.course_key)
    }

    private fun createAddCourseDialog(key: String) {
        val dialog = CustomAddCourseDialog().newInstance(
            "Add Course",
            "Add Course",
            "Cancel Course",
        )
        dialog.show(requireFragmentManager(), "custom courses fragment")
        dialog.onClickListener(object : CustomAddCourseDialog.CustomDialogListener {
            override fun onDialogPositiveClick(str: String) {
                try {

                    val uid = SavedPreferences.user_id
                    Log.e("-- UserId", "onDialogPositiveClick: $uid")
                    Log.e("-- CourseKey", "onDialogPositiveClick: $key")

                    if (uid != null) {
                        coursesCollectionRef.get()
                            // disable add course btn when are than 5 courses are registered
                            .addOnCompleteListener {
                                val param = ArrayMap<String, Any>()
                                param["student_ids"] = FieldValue.arrayUnion(uid)
                                coursesCollectionRef.document(key).set(param, SetOptions.merge())
                            }
                    }
                    sendNotification()
                    Toast.makeText(requireContext(), "Course Successfully Added", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "$e", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDialogNegativeClick(str: String) {
                dialog.dismiss()
            }
        })
    }

    private fun sendNotification() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.w("TAG", "getInstanceId failed", it.exception)
                return@addOnCompleteListener
            }
            val token = it.result.toString()
            val msg = token
            Log.d("TAG", msg)
            FcmNotificationBuilder()
                .type("TYPE_CHAT")
                .title("Course Registration")
                .message("Course successfully registered")
                .uid(SavedPreferences.user_id)
                .firebaseToken(token)
                .receiverFirebaseToken(token)
                .send()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}
