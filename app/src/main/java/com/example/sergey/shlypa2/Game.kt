package com.example.sergey.shlypa2

/**
 * Created by sergey on 3/29/18.
 */
object Game {
    val players = mutableListOf<Player>()
    val teams = mutableListOf<Team>()

    val allWords = mutableListOf<Word>()

    fun addPlayer(player: Player) = players.add(player)

    fun addWord(word: Word) = allWords.add(word)


}