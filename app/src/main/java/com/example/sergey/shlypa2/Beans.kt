package com.example.sergey.shlypa2

/**
 * Created by sergey on 3/29/18.
 */
class Player(name: String, words: MutableList<String>, var scores: Int) {

}

class Team(players: MutableList<Player>, name: String, var scores: Int) {}

class Word(word: String) {
    var play: Boolean = false
    var right: Boolean = false
}