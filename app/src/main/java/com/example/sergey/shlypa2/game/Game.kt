package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.*

/**
 * Created by sergey on 3/29/18.
 */
object Game {

    var state: GameState = GameState()

    var teamNames: MutableList<String> = mutableListOf()

    fun maxTeamsCount(): Int = state.players.size / 2


    fun getSettings(): Settings = state.settings

    fun setSettings(settings: Settings) {
        state.settings = settings
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

    //todo refactor this shit  !!!
    fun setTeams(teams: List<Team>) {
        state.teams.clear()
        state.teams.addAll(teams)
    }

    fun setPlayers(players: List<Player>) {
        state.players.clear()
        players.forEach {
            state.players[it.id] = it
        }
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