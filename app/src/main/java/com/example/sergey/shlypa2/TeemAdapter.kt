package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by sergey on 4/2/18.
 */
class TeemAdapter(): RecyclerView.Adapter<Holder1>(){

     var  teems: List<Team>? = null

    override fun onBindViewHolder(holder: Holder1, position: Int) {
        val teem :Team = teems!![position]
        holder.bind(teem)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder1 {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.holder_teem,parent,false)
        return Holder1(view)
    }

    override fun getItemCount(): Int {
        return if(teems == null) 0 else teems!!.size
    }

    fun setTeem(data : List<Team>?){
        teems = data
        notifyDataSetChanged()}

}

class Holder1(view:View):RecyclerView.ViewHolder(view){
    val teemName   = view.findViewById<TextView>(R.id.teem_name)
    val teemScores = view.findViewById<TextView>(R.id.teem_scores)
    val listPlayers= view.findViewById<TextView>(R.id.list_players)

    fun bind(teem:Team){
        teemName.text=teem.name
        teemScores.text = teem.scores.toString()

        var player = StringBuffer("")
        for (i in 0 until teem.players.size){
            player.append(teem.players[i].name + " ")
        }
        listPlayers.text =player

    }
}