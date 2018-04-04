package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.sergey.shlypa2.game.Player
import com.example.sergey.shlypa2.game.Team
import com.example.sergey.shlypa2.game.Word

/**
 * Created by alex on 4/4/18.
 */

abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view)

class TeamHolder(view: View): BaseHolder(view){
    val teemName   = view.findViewById<TextView>(R.id.teem_name)
    val teemScores = view.findViewById<TextView>(R.id.teem_scores)
    val listPlayers= view.findViewById<TextView>(R.id.list_players)

    fun bind(teem: Team){
        teemName.text=teem.name
        teemScores.text = teem.scores.toString()

        var player = StringBuffer("")
        for (i in 0 until teem.players.size){
            player.append(teem.players[i].name + " ")
        }
        listPlayers.text =player

    }
}

class PlayerHolder(view: View): BaseHolder(view){
    val tvName: TextView = view.findViewById(R.id.playerName)

    fun bind(player : Player){
        tvName.text = player.name
    }
}

class WordsHolder(view: View): BaseHolder(view){
    val tvName: TextView = view.findViewById(R.id.playerName)

    fun bind(word : Word){
        tvName.text = word.word
    }
}