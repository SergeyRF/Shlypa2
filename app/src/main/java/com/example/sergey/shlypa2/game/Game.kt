package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word
import java.util.*

/**
 * Created by sergey on 3/29/18.
 */
object Game {

    var state: GameState = GameState()

    var teamNames: MutableList<String> = mutableListOf()

    fun maxTeamsCount(): Int = state.players.size / 2


    fun addPlayer(player: Player): Boolean {
        val playerExists = state.players.values
                .firstOrNull {
                    it.id == player.id || it.name.equals(player.name, ignoreCase = true)
                } != null

        return if (playerExists) {
            false
        } else {
            state.players[player.id] = player
            true
        }
    }

    fun getSettings(): Settings = state.settings

    fun setSettings(settings: Settings) {
        state.settings = settings
    }

    fun removePlayer(player: Player) {
        state.players.remove(player.id)
    }

    fun reNamePlayer(player: Player) {
        state.players[player.id]?.name = player.name
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

        Collections.shuffle(teamNames)

        for (i in 0 until count) {
            val teamName = teamNames.getOrElse(i, { _ -> "Team $i" })
            state.teams.add(Team(teamName))
        }

        var currentTeam = 0
        while (playersQueue.isNotEmpty()) {
            state.teams[currentTeam].players.add(playersQueue.remove())

            currentTeam = if (currentTeam >= state.teams.size - 1) 0 else currentTeam + 1
        }
    }

    fun getTeams(): List<Team> = state.teams

    fun getRoundResults(): List<TeamWithScores> {

        val teamWithScores = state.teams.map { TeamWithScores(it) }
        val results = state.currentRound?.getResults()

        val scoresMap = results?.getScores() ?: mapOf()

        //Split scores for teams
        teamWithScores.forEach {
            val map = it.scoresMap
            it.team.players.forEach {
                val scores = scoresMap[it.id] ?: 0
                map[it.id] = scores
            }
        }

        return teamWithScores
    }

    fun getGameResults(): List<TeamWithScores> {
        val teamWithScores = state.teams.map { TeamWithScores(it) }
        val resultsMap: List<Map<Long, Int>> = state.resultsList.map { it.getScores() }


        return calculateGameResults(teamWithScores, resultsMap)
    }

    fun calculateGameResults(teamWithScores: List<TeamWithScores>,
                             resultsMap: List<Map<Long, Int>>): List<TeamWithScores> {

        val mapOfPlayers: MutableMap<Long, Int> = mutableMapOf()

        //Sum score for player for each round.
        resultsMap.forEach {
            it.forEach {
                var scores = mapOfPlayers[it.key] ?: 0
                scores += it.value
                mapOfPlayers[it.key] = scores
            }
        }

        teamWithScores.forEach {
            val map = it.scoresMap
            it.team.players.forEach {
                val scores = mapOfPlayers[it.id] ?: 0
                map[it.id] = scores
            }
        }

        return teamWithScores
    }

    fun getCurrentTeam(): Team = state.teams[state.currentTeamPosition]

    fun nextTeam(): Team {
        state.currentTeamPosition++
        if (state.currentTeamPosition >= state.teams.size) state.currentTeamPosition = 0
        return state.teams[state.currentTeamPosition]
    }

    fun addWord(word: Word) = state.allWords.add(word)
    fun addWord(words: List<Word>) = state.allWords.addAll(words)

    fun getWords(): List<Word> = state.allWords

    fun getRound(): Round? = state.currentRound

    fun hasRound() = state.currentRound != null

    fun beginNextRound() {
        val results = state.currentRound?.getResults()
        if (results != null) state.saveRoundResults(results)

        state.currentRoundPosition++
        state.createRound()
    }


    fun portionClear() {
        val newState = GameState()
        newState.players.putAll(state.players)
        newState.teams.addAll(state.teams)
        state = newState
    }

    fun clear() {
        state = GameState()
    }

    fun repeatGame(): Boolean {
        state = GameState().apply {
            players.putAll(state.players)
            teams.addAll(state.teams)
            setSettings(state.settings)
        }
        return state.settings.all_word_random

    }
}