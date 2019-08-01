package com.example.sergey.shlypa2.screens.players.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.getSmallImage
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.holder_player_select.view.*


class ItemPlayerSelect(val player: Player) : AbstractFlexibleItem<ItemPlayerSelect.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
       with(holder) {
           Glide.with(itemView.context)
                   .load(player.getSmallImage(itemView.context))
                   .into(ivAvatar)

           tvName.text = player.name
       }
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemPlayerSelect.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemPlayerSelect

    override fun getLayoutRes() = R.layout.holder_player_select

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {
        val ivAvatar = view.civUserAvatar
        val tvName = view.tvUserName
    }
}