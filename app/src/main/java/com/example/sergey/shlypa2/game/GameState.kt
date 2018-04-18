package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word

/**
 * Created by alex on 4/10/18.
 */
class GameState {

    var gameId = 0

    var settings : Settings = Settings()

    var rounds = listOf(RoundDescriptor("First round", "Simple rules"),
            RoundDescriptor("Second round", "Simple rules"),
            RoundDescriptor("Third round", "Guess word or fuck yourself"))

    val resultsList: MutableList<RoundResults> = mutableListOf()

    val teams = mutableListOf<Team>()
    var currentTeamPosition = 0


    var currentRoundPosition = -1
    var currentRound : Round? = null

    val allWords = mutableListOf<Word>()

    val savedTime = System.currentTimeMillis()

    //todo keep players by id instead of name
    val players = mutableMapOf<String, Player>()

    fun saveRoundResults(results: RoundResults) {
        resultsList.add(results)
    }

    fun createRound() : Boolean {
        if(currentRoundPosition >= rounds.size) return false

        currentRound = Round(allWords)
        currentRound!!.description = rounds[currentRoundPosition].description
        currentRound!!.rules = rounds[currentRoundPosition].rules

        return true
    }

}