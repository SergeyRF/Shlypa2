package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by sergey on 3/29/18.
 */
class PlayerAdapter():RecyclerView.Adapter<Holder>(){

    var player = mutableListOf<Player>()

    override fun onBindViewHolder(holder: Holder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        holder.bind(player[position].name)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.holder_player,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return player.size
    }

    fun setPeople(data : MutableList<Player>){
        player = data
        notifyDataSetChanged()}

}

class Holder(view:View):RecyclerView.ViewHolder(view){
    val tvName: TextView = view.findViewById(R.id.playerName)

    fun bind(string:String){
        tvName.text = string
    }
}