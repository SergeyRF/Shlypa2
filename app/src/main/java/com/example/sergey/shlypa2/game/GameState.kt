package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word

/**
 * Created by alex on 4/10/18.
 */
data class GameState(
        var gameId: Int = 0,
        var settings: Settings = Settings(),
        val resultsList: MutableList<RoundResults> = mutableListOf(),
        val teams: MutableList<Team> = mutableListOf(),
        var currentTeamPosition: Int = 0,
        var currentRoundPosition: Int = -1,
        var currentRound: Round? = null,
        val players: MutableMap<Long, Player> = mutableMapOf(),
        val allWords: MutableList<Word> = mutableListOf(),
        //Serialization
        val savedTime: Long = System.currentTimeMillis(),
        var needToRestore: Boolean = false
) {
    fun saveRoundResults(results: RoundResults) {
        resultsList.add(results)
    }

    fun createRound(): Boolean {
        if (currentRoundPosition >= RoundDescriptors.values().size) {
            currentRound = null
            return false
        }

        currentRound = Round(allWords).apply {
            descriptor = RoundDescriptors.values()[currentRoundPosition]
        }

        return true
    }

}