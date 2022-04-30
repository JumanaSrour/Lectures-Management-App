package com.example.schoolie.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolie.R
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    var handler: Handler = Handler()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setFlags()
        setHandler()
    }

    private fun setHandler() {
        handler.postDelayed({
            checkUserSignIn()
        }, 1000)
    }

    private fun checkUserSignIn() {
        if (auth.currentUser != null) {
            startActivity(Intent(this, HomeStudentActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }
    }


    private fun setFlags() {
        this.window.setFlags(
            WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW,
            WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW
        )
    }
}
