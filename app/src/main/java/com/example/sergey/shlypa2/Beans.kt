package com.example.sergey.shlypa2

/**
 * Created by sergey on 3/29/18.
 */
class Player(val name: String, var words: MutableList<String>? = null, var scores: Int = 0) {
    override fun toString(): String {
        return "Player $name scores $scores"
    }
}

class Team(var name: String) {
    val players : MutableList<Player> = mutableListOf()
    var scores : Int = 0

    override fun toString(): String {
        return "Team $name players: $players"
    }
}

class Word(var word: String) {
    var play: Boolean = false
    var right: Boolean = false
}