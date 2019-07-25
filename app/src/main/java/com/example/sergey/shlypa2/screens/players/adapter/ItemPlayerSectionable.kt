package com.example.sergey.shlypa2.screens.players.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.items.ISectionable
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.holder_player_sectionable.view.*


class ItemPlayerSectionable(
        var teamHeader: ItemTeamSectionable?,
        val player: Player
) : AbstractFlexibleItem<ItemPlayerSectionable.ViewHolder>(), ISectionable<ItemPlayerSectionable.ViewHolder, ItemTeamSectionable> {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
        holder.itemView.tvPlayerName.text = player.name
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemPlayerSectionable.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemPlayerSectionable

    override fun getLayoutRes() = R.layout.holder_player_sectionable

    override fun getHeader() = teamHeader

    override fun setHeader(header: ItemTeamSectionable?) {
        teamHeader = header
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {

    }
}