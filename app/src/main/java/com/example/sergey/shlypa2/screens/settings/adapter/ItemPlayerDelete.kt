package com.example.sergey.shlypa2.screens.settings.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.dpToPx
import com.example.sergey.shlypa2.extensions.getSmallImage
import com.example.sergey.shlypa2.extensions.hide
import com.example.sergey.shlypa2.extensions.show
import com.example.sergey.shlypa2.screens.players.adapter.ItemPlayer
import com.example.sergey.shlypa2.utils.glide.CircleBorderTransform
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class ItemPlayerDelete(playerD: Player)
    : ItemPlayer(playerD) {

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

    override fun equals(other: Any?): Boolean = other is ItemPlayerDelete
}