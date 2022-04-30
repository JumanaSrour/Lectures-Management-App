package com.example.schoolie.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth

class SavedPreferences {
    companion object {
        const val SHARED_EMAIL = "email"
        const val SHARED_USERNAME = "username"
        const val SHARED_PASSWORD = "password"
        const val USER_ID = "user_id"
        const val STUDENT_ID = "student_id"

        private fun getSharedPreference(ctx: Context?): SharedPreferences? {
            return PreferenceManager.getDefaultSharedPreferences(ctx)
        }

        private fun editor(context: Context, const: String, string: String) {
            getSharedPreference(
                context
            )?.edit()?.putString(const, string)?.apply()
        }

        fun getEmail(context: Context) = getSharedPreference(
            context
        )?.getString(SHARED_EMAIL, "")

        fun setEmail(context: Context, email: String) {
            editor(
                context,
                SHARED_EMAIL,
                email
            )
        }

        fun getUser(context: Context) = getSharedPreference(
            context
        )?.getString(USER_ID, FirebaseAuth.getInstance().currentUser?.uid)

        var user_id: String? = ""
            get() = FirebaseAuth.getInstance().currentUser?.uid

        fun getStudent(context: Context) = getSharedPreference(
            context
        )?.getString(STUDENT_ID, FirebaseAuth.getInstance().currentUser?.uid)
        val student_id: String = ""
            get() = field

        var password: String? = ""
            get() = field
            set(value) { field = value }
        fun getPassword(context: Context) = getSharedPreference(
            context
        )?.getString(SHARED_PASSWORD, "")
        fun setPassword(context: Context, password: String) {
            editor(
                context,
                SHARED_PASSWORD,
                password
            )
        }

        fun setUsername(context: Context, username: String) {
            editor(
                context,
                SHARED_USERNAME,
                username
            )
        }

        fun getUsername(context: Context) = getSharedPreference(
            context
        )?.getString(SHARED_USERNAME, "")
    }
}
