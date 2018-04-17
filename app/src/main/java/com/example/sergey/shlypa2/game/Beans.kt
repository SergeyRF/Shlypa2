package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player

/**
 * Created by sergey on 3/29/18.
 */


class Team(var name: String, val players : MutableList<Player> = mutableListOf()) {


    var id = -1

    private var currentPlayer = 0

    fun getPlayer() : Player = players[currentPlayer]

    fun nextPlayer() : Player {
        currentPlayer++
        if (currentPlayer >= players.size) currentPlayer = 0
        return players[currentPlayer]
    }

    override fun toString(): String {
        return "Team $name players: $players"
    }
}

class TeamWithScores(val team : Team) {
    var scores = 0
    //keeps scores for players: key - player id, value - scores
    var scoresMap : MutableMap<Long, Int> = mutableMapOf()
}

class RoundDescriptor(var description : String, var rules : String)

class Settings(var time : Int = 30, var word:Int = 5, var dificult:Dificult = Dificult.EASY, var autoAddWords:Boolean = false)

enum class Dificult{ EASY, MEDIUM, HARD, VERY_HARD }

