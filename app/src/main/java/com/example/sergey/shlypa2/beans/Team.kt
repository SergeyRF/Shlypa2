package com.example.sergey.shlypa2.beans

/**
 * Created by alex on 4/17/18.
 */
class Team(var name: String, var players : MutableList<Player> = mutableListOf()) {


    var id = -1

    var currentPlayer = 0

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