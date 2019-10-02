package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Settings
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word
import java.util.*

data class GameStateOld(
        var gameId: Int = 0,
        var settings : Settings = Settings(),
        val resultsList: MutableList<RoundResults> = mutableListOf(),
        val teams: MutableList<Team> = mutableListOf(),
        var currentTeamPosition: Int = 0,
        var currentRoundPosition: Int = -1,
        var currentRound : RoundOld? = null,
        val players: MutableMap<Long, Player> = mutableMapOf(),
        val allWords: MutableList<Word> = mutableListOf(),
        //Serialization
        val savedTime: Long = System.currentTimeMillis(),
        var needToRestore: Boolean = false
)

class RoundOld(words: List<Word>) {

    var description = 1
    var rules = 2
    var name = 3
    var image = ""

    var wordsQueue: ArrayDeque<Word>
    var wordsAnsweredByPlayer = mutableListOf<Word>()

    var results: MutableMap<Long, Int> = mutableMapOf()

    var currentTeam: Team =  Game.getCurrentTeam()
    var currentPlayer: Player = currentTeam.getPlayer()

    private var currentWord: Word? = null

    var turnFinished = false

    init {
        val listCopy = words.toMutableList()
        Collections.shuffle(listCopy)

        wordsQueue = ArrayDeque()
        wordsQueue.addAll(listCopy)
    }
}