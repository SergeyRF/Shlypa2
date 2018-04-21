package com.example.sergey.shlypa2

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team


/**
 * Created by alex on 4/15/18.
 */
object TestDataProvider {

    fun getTestPlayers(count : Int) : MutableList<Player> {
        val playersList : MutableList<Player> = mutableListOf()

        for(i in 0 until count) {
            playersList.add(Player("Test player $i", id = i.toLong()))
        }

        return playersList
    }

    fun getTestTeams(teamCount : Int, playersInTeam : Int) : List<Team> {
        val teamsList : MutableList<Team> = mutableListOf()
        for(i in 0 until teamCount) {
            teamsList.add(Team("Test team $i", getTestPlayers(playersInTeam)))
        }

        return teamsList
    }
}