package com.example.sergey.shlypa2.screens.players.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Team
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractExpandableHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.items.IHeader
import eu.davidea.viewholders.ExpandableViewHolder
import kotlinx.android.synthetic.main.holder_team_sectionable.view.*


class ItemTeamSectionable(val team: Team) : AbstractExpandableHeaderItem<ItemTeamSectionable.ViewHolder, ItemPlayerSectionable>(), IHeader<ItemTeamSectionable.ViewHolder> {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
        holder.itemView.tvTeamName.text = team.name
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemTeamSectionable.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemTeamSectionable

    override fun getLayoutRes() = R.layout.holder_team_sectionable

    override fun isDraggable() = false

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : ExpandableViewHolder(view, adapter) {

    }
}