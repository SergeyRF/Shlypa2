package com.example.sergey.shlypa2.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R

/**
 * Created by alex on 4/21/18.
 */
class RvImageAdapter() : androidx.recyclerview.widget.RecyclerView.Adapter<ImageHolder>() {

    private var data: List<String>? = null

    var listener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_image, parent, false)
        return ImageHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(data!![position])
        holder.listener = listener
    }

    fun setData(list: List<String>?) {
        data = list
        notifyDataSetChanged()
    }
}

class ImageHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val iv: ImageView = view.findViewById(R.id.ivImageDialog)

    var listener: ((String) -> Unit)? = null

    fun bind(imageName: String) {
        val link = Functions.imageNameToUrl("player_avatars/small/$imageName")
        Glide.with(itemView)
                .load(link)
                .into(iv)

        view.setOnClickListener { listener?.invoke(imageName) }
    }
}