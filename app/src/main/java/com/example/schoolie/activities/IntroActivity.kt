package com.example.schoolie.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolie.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        btn_lecturer_intro.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        btn_student_intro.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
