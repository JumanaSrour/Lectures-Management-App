package com.example.schoolie.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.schoolie.R
import com.example.schoolie.models.Lecture
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.lectures_card_item.view.*

class LecturesAdapter(val context: Context, val data: ArrayList<Lecture>) :
    RecyclerView.Adapter<LecturesAdapter.ViewHolder>() {
    private var listener: SetClickListener? = null
    private lateinit var analytics: FirebaseAnalytics
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lecImage: ImageView = itemView.lectureImage
        var lecName: TextView = itemView.lectureName
        var lecCard: CardView = itemView.cv_lectures
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lectures_card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        analytics = FirebaseAnalytics.getInstance(context)
        val bundle = Bundle()
        holder.lecName.text = data[position].lecture_title
        holder.lecCard.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: clicked")
            listener?.onItemClickListener(position, data[position])
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, data[position].lecture_title)
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)
        }
        Glide.with(context).load(data[position].lecture_image).circleCrop().into(holder.lecImage)
    }

    public fun setListener(listener: SetClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = data.size

    interface SetClickListener {
        fun onItemClickListener(position: Int, lecture: Lecture)
    }
}
