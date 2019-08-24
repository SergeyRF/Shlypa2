package com.example.sergey.shlypa2.screens.game.adapter


import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.game.TeamWithScores
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.holder_team_score.view.*


class ItemTeamWithScores(
        val team: TeamWithScores,
        val playersPool: RecyclerView.RecycledViewPool) : AbstractFlexibleItem<ItemTeamWithScores.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
        with(holder) {
            tvTeamName.text = team.team.name
            tvTeamScores.text = team.getScores().toString()

            val playersWithScores = team.team.players.map {
                it to team.scoresMap[it.id]
            }.map {
                ItemPlayerWithScores(it.first, it.second ?: 0)
            }

            playersAdapter.updateDataSet(playersWithScores)
        }
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ViewHolder(view, adapter, playersPool)

    override fun equals(other: Any?) = other is ItemTeamWithScores
            && other.team == team

    override fun getLayoutRes() = R.layout.holder_team_score

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                     viewPool: RecyclerView.RecycledViewPool)
        : FlexibleViewHolder(view, adapter) {

        val playersAdapter = FlexibleAdapter(emptyList())

        val tvTeamName = view.tvTeamName
        val tvTeamScores = view.tvTeamScores
        val listPlayers = view.rvPlayers.apply {
            layoutManager = LinearLayoutManager(view.context)
            this.adapter = playersAdapter
            setRecycledViewPool(viewPool)
        }
    }
}