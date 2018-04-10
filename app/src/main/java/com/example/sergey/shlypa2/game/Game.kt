package com.example.sergey.shlypa2.game

import timber.log.Timber
import java.util.*

/**
 * Created by sergey on 3/29/18.
 */
object Game {

    var state : GameState = GameState()
    var time = 60
    var words = 5

    var currentRound: Round? = null

    fun maxTeamsCount(): Int = state.players.size / 2


    fun addPlayer(player: Player): Boolean {
        return if (state.players.containsKey(player.name)) {
            false
        } else {
            state.players[player.name] = player
            true
        }
    }

    fun removePlayer(player: Player) {
        state.players.remove(player.name)
    }

    fun getPlayers(): List<Player> {
        return state.players.values.toList()
    }

    fun createTeams(count: Int) {
        val shuffledPlayers: MutableList<Player> = state.players.values.toMutableList()
        Collections.shuffle(shuffledPlayers)

        val playersQueue = ArrayDeque<Player>()
        playersQueue.addAll(shuffledPlayers)

        // why not?
        state.teams.clear()

        for (i in 0 until count) {
            state.teams.add(Team("Team $i"))
        }

        var currentTeam = 0
        while (playersQueue.isNotEmpty()) {
            state.teams[currentTeam].players.add(playersQueue.remove())

            currentTeam = if (currentTeam >= state.teams.size - 1) 0 else currentTeam + 1
        }
    }

    fun getTeams(): List<Team> = state.teams

    fun getCurrentTeam(): Team = state.teams[state.currentTeamPosition]

    fun nextTeam(): Team {
        state.currentTeamPosition++
        if (state.currentTeamPosition >= state.teams.size) state.currentTeamPosition = 0
        return state.teams[state.currentTeamPosition]
    }

    fun addWord(word: Word) = state.allWords.add(word)

    fun getWords(): List<Word> = state.allWords

    fun getRound(): Round = currentRound!!

    fun hasRound() = currentRound != null

    fun beginNextRound() {
        state.currentRoundPosition++
        currentRound = state.createRound()
    }

    fun finishRound() {
        currentRound?.countScores()
        currentRound = null
    }

    fun clear() {
        state = GameState()
    }
}