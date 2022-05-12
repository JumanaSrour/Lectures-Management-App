package com.example.schoolie.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.schoolie.R
import com.example.schoolie.activities.student.ProfileActivity
import com.example.schoolie.adapters.CoursesAdapter
import com.example.schoolie.fragments.HomeFragment
import com.example.schoolie.fragments.StudentCoursesFragment
import com.example.schoolie.fragments.StudentFavoriteFragment
import com.example.schoolie.models.Course
import com.example.schoolie.models.User
import com.example.schoolie.utilities.SavedPreferences
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_student_courses.*

class HomeStudentActivity : AppCompatActivity(), CoursesAdapter.SetClickListener {
    private var analytics: FirebaseAnalytics? = null
    private lateinit var coursesAdapter: CoursesAdapter
    private lateinit var coursesFragment: StudentCoursesFragment
    private lateinit var favoriteFragment: StudentFavoriteFragment
    private lateinit var homeFragment: HomeFragment
    private val TAG = "MainActivity"
    private lateinit var mAdView: AdView
    private val courseCollectionRef = Firebase.firestore.collection("courses")
    private val userCollectionRef = Firebase.firestore.collection("user")
    private lateinit var auth: FirebaseAuth
    private lateinit var userImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        analytics = FirebaseAnalytics.getInstance(this)
        showAds()
        initProperties()
        setEventsListener()
        retrieveUser()
        bottomNavView.selectedItemId = R.id.home
        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    setCurrentFragment(homeFragment)
                    sv_search_std.visibility = View.GONE
                    tv_std_home.text = getString(R.string.home)
                    UiTrack("Home", "Home")
                }
                R.id.courses -> {
                    setCurrentFragment(coursesFragment)
                    sv_search_std.visibility = View.VISIBLE
                    tv_std_home.text = getString(R.string.courses)
                    UiTrack("Courses", "Courses")
                }
                R.id.favorites -> {
                    setCurrentFragment(favoriteFragment)
                    sv_search_std.visibility = View.GONE
                    tv_std_home.text = getString(R.string.favorites)
                    UiTrack("Favorites", "Favorites")
                }
                R.id.chat -> {
                    startActivity(Intent(this, ContactsActivity::class.java))
                    UiTrack("Chat", "Chat")
                }
            }
            true
        }
    }

    private fun showAds() {
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                val toastMessage: String = "ad fail to load"
                Toast.makeText(applicationContext, toastMessage.toString(), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                val toastMessage: String = "ad loaded"
                Toast.makeText(applicationContext, toastMessage.toString(), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onAdOpened() {
                super.onAdOpened()
                val toastMessage: String = "ad is open"
                Toast.makeText(applicationContext, toastMessage.toString(), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                val toastMessage: String = "ad is clicked"
                Toast.makeText(applicationContext, toastMessage.toString(), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                val toastMessage: String = "ad is closed"
                Toast.makeText(applicationContext, toastMessage.toString(), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                val toastMessage: String = "ad impression"
                Toast.makeText(applicationContext, toastMessage.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun retrieveUser() {
        val uid = SavedPreferences.user_id
        userImage = profile_std_home

        if (uid != "") {
            userCollectionRef.document(uid!!).get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val user = document.toObject<User>()
                        Glide.with(this).load(user!!.user_image).circleCrop()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun setEventsListener() {
        profile_std_home.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        notifications_std_home.setOnClickListener {
        }
        sv_search_std.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // ignore this
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // ignore this
            }

            override fun afterTextChanged(p0: Editable?) {
                val uid = SavedPreferences.user_id
                if (uid != "" /*&& sv_search_std.text.toString().isNotEmpty()*/) {
                    Log.e("search", "setEventsListener: ${sv_search_std.text}")
                    getDataFromServer()
                }
            }

            private fun getDataFromServer() {
                courseCollectionRef.get()
                    .addOnCompleteListener {
                        val array = arrayListOf<Course>()
                        for (course in it.result.documents) {
                            val obj = course.toObject<Course>()
                            array.add(obj!!)
                        }
                        val newArray = ArrayList(
                            array.filter {
                                it.course_name.trim().lowercase()
                                    .contains(sv_search_std.text.toString().trim().lowercase())
                            }
                        )
                        Log.d("documents", "${sv_search_std.text}")
                        setDataInAdapter(newArray)
                    }
                    .addOnFailureListener { exception ->
                        Log.d("---", "get failed with ", exception)
                    }
            }

            private fun setDataInAdapter(newArray: ArrayList<Course>) {
                coursesAdapter = CoursesAdapter(this@HomeStudentActivity, null, newArray)
                coursesAdapter.setListener(this@HomeStudentActivity)
                rv_courses_std.adapter = coursesAdapter
                coursesAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun initProperties() {
        auth = Firebase.auth
        homeFragment = HomeFragment()
        favoriteFragment = StudentFavoriteFragment()
        coursesFragment = StudentCoursesFragment()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(com.example.schoolie.R.id.flFragment, fragment)
                .commit()
        }

    override fun onItemClickListener(position: Int, course: Course) {
        Log.d("tag", "clicked")
    }

    override fun onButtonClickListener(position: Int, course: Course) {
        Log.d("tag", "clicked")
    }

    private fun UiTrack(screenName: String, screenClass: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        analytics!!.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
