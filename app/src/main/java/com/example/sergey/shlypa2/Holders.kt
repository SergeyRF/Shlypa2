package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.Team
import com.example.sergey.shlypa2.game.TeamWithScores


/**
 * Created by alex on 4/4/18.
 */

abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view)

class TeamHolder(view: View) : BaseHolder(view) {
    val teamName = view.findViewById<TextView>(R.id.team_name)
    val listPlayers = view.findViewById<TextView>(R.id.tvPlayers)

    fun bind(teem: Team) {
        teamName.text = teem.name

        var player = StringBuffer("")
        for (i in 0 until teem.players.size) {
            player.append(teem.players[i].name + "\n")
        }
        listPlayers.text = player

    }
}

class TeamWithScoreHolder(val view: View) : BaseHolder(view) {
    private val teamName : TextView = view.findViewById(R.id.team_name)
    private val teemScores : TextView = view.findViewById(R.id.tvTeamScores)
    private val listPlayers : LinearLayout = view.findViewById(R.id.linearPlayers)

    fun bind(scoredTeam: TeamWithScores) {
        teamName.text = scoredTeam.team.name
        teemScores.text = scoredTeam.scoresMap.values.sum().toString()

        listPlayers.removeAllViews()

        for(player in scoredTeam.team.players) {

            val playerView = LayoutInflater.from(view.context)
                    .inflate(R.layout.holder_player_score, listPlayers, false)

            val playerHolder = PlayerWithScoreHolder(playerView)
            playerHolder.bind(player, scoredTeam.scoresMap[player.id] ?: 0)

            listPlayers.addView(playerView)
        }
    }
}

class PlayerHolder(view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.playerName)

    fun bind(player: Player) {
        tvName.text = player.name
    }
}

class PlayerWithScoreHolder(view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvScores: TextView = view.findViewById(R.id.tvScores)

    fun bind(player: Player, scores: Int) {
        tvName.text = player.name
        tvScores.text = scores.toString()
    }
}

class WordsHolder(view: View) : BaseHolder(view) {
    val tvName: TextView = view.findViewById(R.id.playerName)

    fun bind(word: Word) {
        tvName.text = word.word
    }
}

class WordResaltHolder(view: View):BaseHolder(view){

}