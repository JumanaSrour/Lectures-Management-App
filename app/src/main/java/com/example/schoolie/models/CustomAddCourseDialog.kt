package com.example.schoolie.models

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.schoolie.R
import kotlinx.android.synthetic.main.custom_add_course_dialog.*

class CustomAddCourseDialog : DialogFragment() {

    private var title: String? = null
    private var btnAddCourse: String? = null
    private var btnCancelCourse: String? = null
    private var listener: CustomDialogListener? = null

    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun newInstance(
        title: String,
        btnAddCourse: String,
        btnCancelCourse: String
    ): CustomAddCourseDialog {
        val args = Bundle()
        args.putString("title", title)
        args.putString("btnAddCourse", btnAddCourse)
        args.putString("btnCancelCourse", btnCancelCourse)
        val fragment = CustomAddCourseDialog()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_add_course_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        if (!TextUtils.isEmpty(title)) {
            tv_alert_title.visibility = View.VISIBLE
            tv_alert_title.text = title
        } else {
            tv_alert_title.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(btnAddCourse)) {
            btn_add_course.visibility = View.VISIBLE
            btn_add_course.text = btnAddCourse
        } else {
            btn_add_course.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(btnCancelCourse)) {
            btn_cancel_course.visibility = View.VISIBLE
            btn_cancel_course.text = btnCancelCourse
        } else {
            btn_cancel_course.visibility = View.GONE
        }

//        problem : how to set listener to button
        btn_add_course.setOnClickListener {
            listener?.onDialogPositiveClick("Success")
        }
        btn_cancel_course.setOnClickListener {
            listener?.onDialogNegativeClick("Fail")
        }
    }

    fun onClickListener(listener: CustomDialogListener) {
        this.listener = listener
    }

    interface CustomDialogListener {
        fun onDialogPositiveClick(str: String)
        fun onDialogNegativeClick(str: String)
    }

    private fun getArgs() {
        arguments?.getString("title")?.let {
            title = it
        }
        arguments?.getString("btnAddCourse")?.let {
            btnAddCourse = it
        }
        arguments?.getString("btnCancelCourse")?.let {
            btnCancelCourse = it
        }
    }
}
