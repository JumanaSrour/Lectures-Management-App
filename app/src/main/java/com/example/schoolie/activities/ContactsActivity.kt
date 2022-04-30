package com.example.schoolie.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.schoolie.R
import com.example.schoolie.fragments.ContactsFragment
import com.example.schoolie.models.User
import com.example.schoolie.utilities.SavedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : AppCompatActivity() {
    private lateinit var contactsFragment: ContactsFragment
    private val userCollectionRef = Firebase.firestore.collection("user")
    private lateinit var auth: FirebaseAuth
    private lateinit var userImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        retrieveUser()

        contactsFragment = ContactsFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, contactsFragment)
                .commit()
        }
    }

    private fun retrieveUser() {
        auth = FirebaseAuth.getInstance()
        userImage = profile_std_chat
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
}
