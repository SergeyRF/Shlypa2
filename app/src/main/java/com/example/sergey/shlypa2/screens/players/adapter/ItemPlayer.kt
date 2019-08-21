package com.example.sergey.shlypa2.screens.players.adapter


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.dpToPx
import com.example.sergey.shlypa2.extensions.getSmallImage
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.glide.CircleBorderTransform
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.holder_player.view.*


open class ItemPlayer(val player: Player)
    : AbstractFlexibleItem<ItemPlayer.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
        with(holder) {
            tvName.text = player.name

            Glide.with(itemView)
                    .load(player.getSmallImage(itemView.context))
                    .apply(RequestOptions()
                            .transforms(CircleCrop(), CircleBorderTransform(borderColor, 1.dpToPx)))
                    .into(avatarImage)
        }
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemPlayer.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemPlayer

    override fun getLayoutRes() = R.layout.holder_player

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {
        val tvName: TextView = itemView.tvPlayerName
        val avatarImage: ImageView = itemView.civPlayerAvatar

        val borderColor = Functions.getThemedBgColor(itemView.context)
    }
}