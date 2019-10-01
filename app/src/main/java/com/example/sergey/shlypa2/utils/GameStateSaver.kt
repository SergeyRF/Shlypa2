package com.example.sergey.shlypa2.utils

import android.content.Context
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.GameStateOld
import com.example.sergey.shlypa2.game.Round
import com.example.sergey.shlypa2.game.RoundDescriptors
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.File

class GameStateSaver(context: Context) {

    private val folderName = "/gameStates"
    private val pFile = "/states"
    private val gson = Gson()
    private val file: File by lazy {
        val fileDir = File(context.filesDir, folderName).apply { mkdirs() }
        File(fileDir, pFile)
    }
    private val savedGamesLimit = 10


    private fun saveState(gameState: List<GameState>) {
        runCatching {
            file.writeText(gson.toJson(gameState))
        }.onFailure {
            Timber.e(it)
        }
    }

    @Synchronized
    fun loadState(): MutableList<GameState> {
        runCatching {
            val json = file.readText()
            return gson.fromJson(json, object : TypeToken<MutableList<GameState>>() {}.type)
        }.onFailure {
            Timber.e(it)
            return mutableListOf()
        }
        return mutableListOf()
    }

    @Synchronized
    fun getLastSavedState(): GameState? = loadState().maxBy { it.savedTime }

    @Synchronized
    fun insertState(state: GameState) {
        val savedGames = loadState()
                .asSequence()
                .distinctBy { it.gameId }
                .filter { it.gameId != state.gameId }
                .sortedByDescending { it.savedTime }
                .take(savedGamesLimit - 1)
                .toList()
                .toMutableList()

        savedGames.add(state.copy(savedTime = System.currentTimeMillis()))
        saveState(savedGames)
    }

    @Synchronized
    fun replaceStates(states: List<GameStateOld>) {
        saveState(states.map { gameStateOldToNew(it) })
    }

    fun deleteState(gameId: Int) {
        val savedState = loadState()
        savedState.remove(savedState.firstOrNull { it.gameId == gameId })
        saveState(savedState)
    }

    private fun gameStateOldToNew(state: GameStateOld): GameState {
        val round = state.currentRound?.let { r ->
            Round(emptyList()).apply {
                wordsQueue = r.wordsQueue
                wordsAnsweredByPlayer = r.wordsAnsweredByPlayer
                results = r.results
                currentTeam = r.currentTeam
                descriptor = when(r.image) {
                    "megaphone.png" -> RoundDescriptors.WORD_BY_SENTENCES
                    "silence.png" -> RoundDescriptors.WORD_BY_GESTURES
                    else -> RoundDescriptors.WORD_BY_WORD
                }
                turnFinished = r.turnFinished
            }
        }

        with(state) {
            return GameState(
                  gameId,
                    settings,
                    resultsList,
                    teams,
                    currentTeamPosition,
                    currentRoundPosition,
                    round,
                    players,
                    allWords,
                    savedTime,
                    needToRestore
            )
        }
    }

}

