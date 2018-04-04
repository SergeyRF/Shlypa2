package com.example.sergey.shlypa2.game

import timber.log.Timber
import java.util.*

/**
 * Created by sergey on 3/29/18.
 */
object Game {
    private val players = mutableMapOf<String, Player>()

    private val rounds = listOf(RoundDescriptor("First round", "Simple rules"),
            RoundDescriptor("Second round", "Simple rules"),
            RoundDescriptor("Third round", "Guess word or fuck yourself"))

    private val teams = mutableListOf<Team>()
    private var currentTeamPosition = 0

    private var currentRound: Round? = null
    private var currentRoundPosition = -1

    private val allWords = mutableListOf<Word>()
    var time = 60
    var words = 5


    fun maxTeamsCount(): Int = players.size / 2


    fun addPlayer(player: Player): Boolean {
        return if (players.containsKey(player.name)) {
            false
        } else {
            players[player.name] = player
            true
        }
    }

    fun removePlayer(player: Player) {
        players.remove(player.name)
    }

    fun getPlayers(): List<Player> = players.values.toList()

    fun createTeams(count: Int) {
        val shuffledPlayers: MutableList<Player> = players.values.toMutableList()
        Collections.shuffle(shuffledPlayers)

        val playersQueue = ArrayDeque<Player>()
        playersQueue.addAll(shuffledPlayers)

        for (i in 0 until count) {
            teams.add(Team("Team $i"))
        }

        var currentTeam = 0
        while (playersQueue.isNotEmpty()) {
            teams[currentTeam].players.add(playersQueue.remove())

            currentTeam = if (currentTeam >= teams.size - 1) 0 else currentTeam + 1
        }
    }

    fun getTeams(): List<Team> = teams

    fun getCurrentTeam(): Team = teams[currentTeamPosition]

    fun nextTeam(): Team {
        currentTeamPosition++
        if (currentTeamPosition >= teams.size) currentTeamPosition = 0
        return teams[currentTeamPosition]
    }

    fun addWord(word: Word) = allWords.add(word)

    fun getWords(): List<Word> = allWords

    fun getRound(): Round = currentRound!!

    fun hasRound() = currentRound != null

    fun beginNextRound() {
        currentRoundPosition++
        Timber.d("current round $currentRoundPosition rounds ${rounds.size}")
        if (currentRoundPosition < rounds.size) {
            currentRound = Round(allWords)
            currentRound?.description = rounds[currentRoundPosition].description
            currentRound?.rules = rounds[currentRoundPosition].rules
        }
    }

    fun finishRound() {
        currentRound?.countScores()
        currentRound = null
    }

    fun clear() {
        allWords.clear()
        teams.clear()
        players.clear()
    }
}