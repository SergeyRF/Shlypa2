package com.example.sergey.shlypa2.screens.game.adapter


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
import kotlinx.android.synthetic.main.holder_player_inteam.view.*


class ItemPlayerWithScores(val player: Player, val scores: Int) : AbstractFlexibleItem<ItemPlayerWithScores.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
        with(holder) {
            Glide.with(itemView)
                    .load(player.getSmallImage(itemView.context))
                    .into(ivPlayer)

            tvName.text = player.name
            tvScores.text = scores.toString()
        }
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemPlayerWithScores.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemPlayerWithScores
            && other.player == player
            && other.scores == scores

    override fun getLayoutRes() = R.layout.holder_player_inteam

    override fun hashCode(): Int {
        var result = player.hashCode()
        result = 31 * result + scores
        return result
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {
        val ivPlayer = view.civPlayerAvatar
        val tvName = view.tvName
        val tvScores = view.tvScores
    }
}