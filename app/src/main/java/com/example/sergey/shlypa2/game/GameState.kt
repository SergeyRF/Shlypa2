package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word

/**
 * Created by alex on 4/10/18.
 */
data class GameState(
        var gameId: Int = 0,
        var settings : Settings = Settings(),
        val resultsList: MutableList<RoundResults> = mutableListOf(),
        val teams: MutableList<Team> = mutableListOf(),
        var currentTeamPosition: Int = 0,
        var currentRoundPosition: Int = -1,
        var currentRound : Round? = null,
        val players: MutableMap<Long, Player> = mutableMapOf(),
        val allWords: MutableList<Word> = mutableListOf(),
        //Serialization
        val savedTime: Long = System.currentTimeMillis(),
        var needToRestore: Boolean = false
) {
    var rounds = listOf(RoundDescriptor(R.string.round_first_number, R.string.round_first,
            R.string.round_first_name, "megaphone.png"),
            RoundDescriptor(R.string.round_two_number, R.string.round_two,
                    R.string.round_two_name,"silence.png"),
            RoundDescriptor(R.string.round_three_number,
                    R.string.round_three, R.string.round_three_name,
                    "one.png"))



    fun saveRoundResults(results: RoundResults) {
        resultsList.add(results)
    }

    fun createRound() : Boolean {
        if(currentRoundPosition >= rounds.size) {
            currentRound = null
            return false
        }

        currentRound = Round(allWords).apply {
            val descriptor = rounds[currentRoundPosition]
            description = descriptor.description
            rules = descriptor.rules
            name = descriptor.name
            image = descriptor.image
        }

        return true
    }

}