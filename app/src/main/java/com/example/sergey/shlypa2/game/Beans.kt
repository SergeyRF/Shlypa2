package com.example.sergey.shlypa2.game

/**
 * Created by sergey on 3/29/18.
 */
class Player(val name: String, var words: MutableList<String>? = null, var scores: Int = 0, var id : Int = -1) {

    override fun toString(): String {
        return "Player $name scores $scores"
    }
}

class Team(var name: String) {
    val players : MutableList<Player> = mutableListOf()
    var scores : Int = 0

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

class Word(var word: String) {
    var play: Boolean = false
    var right: Boolean = false

    var answeredBy = -1
    var addedBy = -1
}

