package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sergey.shlypa2.game.Player

/**
 * Created by sergey on 3/29/18.
 */
class PlayerAdapter():RecyclerView.Adapter<Holder>(){

    var players: List<Player>?= null

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val player : Player = players!![position]
        holder.bind(player.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.holder_player,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return if(players == null) 0 else players!!.size
    }

    fun setPeople(data : List<Player>?){
        players = data
        notifyDataSetChanged()}

}

class Holder(view:View):RecyclerView.ViewHolder(view){
    val tvName: TextView = view.findViewById(R.id.playerName)

    fun bind(string:String){
        tvName.text = string
    }
}