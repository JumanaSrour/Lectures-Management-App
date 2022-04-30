package com.example.schoolie.activities.lecturer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.schoolie.R
import com.example.schoolie.fragments.lecturer.AssignmentsFragment
import com.example.schoolie.fragments.lecturer.LecturerCoursesFragment
import com.example.schoolie.models.User
import com.example.schoolie.utilities.SavedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home_lecturer.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomNavView

class HomeLecturerActivity : AppCompatActivity() {
    private lateinit var coursesFragment: LecturerCoursesFragment
    private lateinit var assignmentsFragment: AssignmentsFragment
    private val userCollectionRef = Firebase.firestore.collection("lecturer")
    private lateinit var auth: FirebaseAuth
    private lateinit var userImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_lecturer)
        initProperties()
        setEventsListener()
        retrieveUser()
        userImage = profile_std_home
        bottomNavView.selectedItemId = R.id.home
        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.courses -> {
                    setCurrentFragment(coursesFragment)
                    sv_search_lec.visibility = View.VISIBLE
                    tv_std_home.text = getString(R.string.courses)
                }
                R.id.lec_assignments -> {
                    setCurrentFragment(assignmentsFragment)
                    sv_search_lec.visibility = View.GONE
                    tv_std_home.text = getString(R.string.assignments)
                }
            }
            true
        }
    }

    private fun retrieveUser() {
        val uid = SavedPreferences.user_id
        if (uid != "") {
            userCollectionRef.document(uid!!).get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val user = document.toObject<User>()
                        Glide.with(this).load(Uri.parse(user!!.user_image)).circleCrop()
                            .into(userImage)
                    } else {
                        Log.d("---", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("---", "get failed with ", exception)
                }
        }
    }

    private fun setEventsListener() {
        profile_lec_home.setOnClickListener {
            startActivity(Intent(this, LecturerProfileActivity::class.java))
        }
    }

    private fun initProperties() {
        auth = Firebase.auth
        assignmentsFragment = AssignmentsFragment()
        coursesFragment = LecturerCoursesFragment()
    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
                .commit()
        }
}
