package com.example.schoolie.activities.lecturer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.schoolie.R
import com.example.schoolie.activities.ChangePasswordActivity
import com.example.schoolie.activities.LoginActivity
import com.example.schoolie.models.User
import com.example.schoolie.utilities.SavedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_lecturer_profile.*

class LecturerProfileActivity : AppCompatActivity() {
    private lateinit var profileImage: ImageView
    private val lecturerCollectionRef = Firebase.firestore.collection("lecturer")
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecturer_profile)
        initProperties()
        setEventsListeners()
        retrieveUser()
        profileImage = iv_lec_profile
    }

    private fun retrieveUser() {
        val uid = SavedPreferences.user_id

        if (uid != "") {
            lecturerCollectionRef.document(uid!!).get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val user = document.toObject<User>()
                        tv_lec_name.text = user!!.username
                        tv_lec_email.text = user.email
                        Glide.with(this).load(Uri.parse(user.user_image)).circleCrop().into(iv_lec_profile)
                    } else {
                        Log.d("---", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("---", "get failed with ", exception)
                }
        }
    }

    private fun setEventsListeners() {
        btn_lec_change_password.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
            finish()
        }
        btn_lec_edit_profile.setOnClickListener {
            startActivity(Intent(this, LecturerEditProfileActivity::class.java))
            finish()
        }
        lec_back_profile.setOnClickListener {
            startActivity(Intent(this, HomeLecturerActivity::class.java))
        }
        tv_lec_logout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun initProperties() {
        auth = FirebaseAuth.getInstance()
    }
}
