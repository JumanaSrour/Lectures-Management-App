package com.example.schoolie.models

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.example.schoolie.R
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_image_message.view.*

class ImageMessageItem(
    val message: ImageMessage,
    val context: Context
) :
    MessageItem(message) {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)
        Glide.with(context)
            .load(message.imagePath).centerCrop()
            .placeholder(R.drawable.ic_image)
            .into(viewHolder.itemView.imageView_message_image)
    }

    override fun getLayout() = R.layout.item_image_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ImageMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ImageMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}
