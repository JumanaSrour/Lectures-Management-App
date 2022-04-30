package com.example.schoolie.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schoolie.R
import com.example.schoolie.adapters.CoursesAdapter
import kotlinx.android.synthetic.main.fragment_home_student.*

class HomeFragment : Fragment() {
    private lateinit var coursesAdapter: CoursesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_student, container, false)
    }
}
