package com.example.sergey.shlypa2.screens.players.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.views.HolderInflater
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.holder_teem.view.*


class ItemTeam(val team: Team,
               val onRenameClick: (Team) -> Unit) : AbstractFlexibleItem<ItemTeam.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
        with(holder) {
            teamName.text = team.name

            playersList.removeAllViews()
            playersList.addView(HolderInflater.inflatePlayers(team.players, itemView.context))
            ivRename.setOnClickListener {
                onRenameClick.invoke(team)
            }
            teamName.setOnLongClickListener {
                onRenameClick.invoke(team)
                true
            }
        }
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemTeam.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemTeam
            && other.team == team

    override fun getLayoutRes() = R.layout.holder_teem

    override fun hashCode(): Int {
        var result = team.hashCode()
        result = 31 * result + onRenameClick.hashCode()
        return result
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {

        val teamName = itemView.tvTeamName
        val playersList = itemView.llPlayers
        val ivRename = itemView.ibTeemRename
    }
}