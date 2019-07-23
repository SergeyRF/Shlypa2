package com.example.sergey.shlypa2.screens.players.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.getSmallImage
import de.hdodenhof.circleimageview.CircleImageView

class RvAdapterPlayerSelect : RecyclerView.Adapter<RvAdapterPlayerSelect.Holder>() {

    private var data: List<Player>? = null

    var listener: ((Player) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_player_select, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data!![position])
    }

    fun setData(list: List<Player>?) {
        data = list
        notifyDataSetChanged()
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        val avatar: CircleImageView = view.findViewById(R.id.civUserAvatar)
        val name: TextView = view.findViewById(R.id.tvUserName)

        fun bind(player: Player) {
            Glide.with(view.context)
                    .load(player.getSmallImage(view.context))
                    .into(avatar)

            name.text = player.name

            view.setOnClickListener { listener?.invoke(player) }
        }
    }
}

