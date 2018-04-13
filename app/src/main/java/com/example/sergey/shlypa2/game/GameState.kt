package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word

/**
 * Created by alex on 4/10/18.
 */
class GameState {
    var rounds = listOf(RoundDescriptor("First round", "Simple rules"),
            RoundDescriptor("Second round", "Simple rules"),
            RoundDescriptor("Third round", "Guess word or fuck yourself"))

    val teams = mutableListOf<Team>()
    var currentTeamPosition = 0


    var currentRoundPosition = -1

    val allWords = mutableListOf<Word>()

    val players = mutableMapOf<String, Player>()

    fun createRound() : Round? {
        if(currentRoundPosition >= rounds.size) return null

        val round = Round(allWords)
        round.description = rounds[currentRoundPosition].description
        round.rules = rounds[currentRoundPosition].rules

        return round
    }

}