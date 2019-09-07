package com.example.sergey.shlypa2.utils

import android.content.Context
import com.example.sergey.shlypa2.game.GameState
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
    private val savedGamesLimit = 5


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
                .sortedBy { it.savedTime }
                .takeLast(savedGamesLimit - 1)
                .toMutableList()

        savedGames.add(state)
        saveState(savedGames)
    }

    fun deleteState(gameId: Int) {
        val savedState = loadState()
        savedState.remove(savedState.firstOrNull { it.gameId == gameId })
        saveState(savedState)
    }

}

