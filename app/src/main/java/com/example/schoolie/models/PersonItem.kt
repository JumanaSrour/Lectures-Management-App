package com.example.schoolie.models

import android.content.Context
import com.bumptech.glide.Glide
import com.example.schoolie.R
import com.example.schoolie.utilities.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.chatlist_item.*
import kotlinx.android.synthetic.main.chatlist_item.view.*

class PersonItem(
    val person: User,
    val userId: String,
    private val context: Context
) :
    Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_name.text = person.username
        viewHolder.itemView.tv_bio.text = person.email
        if (person.user_image != null)
            Glide.with(context)
                .load(person.user_image).circleCrop()
                .placeholder(R.drawable.ic_avatar)
                .into(viewHolder.itemView.imageView_profile_picture)
    }

    override fun getLayout() = R.layout.chatlist_item
}
