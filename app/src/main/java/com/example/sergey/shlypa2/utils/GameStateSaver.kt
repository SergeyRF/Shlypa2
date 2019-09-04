package com.example.sergey.shlypa2.utils

import android.content.Context
import com.example.sergey.shlypa2.game.GameState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.File
import java.io.FileInputStream

class GameStateSaver(context: Context) {

    private val fileName = "/gameState"
    private val pFile = "/state"
    private val gson = Gson()
    private var file: File? = null
    private val savedCount = 5

    init {
        val fileDir = File(context.filesDir, fileName)
        fileDir.mkdirs()
        file = File(fileDir, pFile)
    }

    private fun saveState(gameState: List<GameState>) {

        file?.printWriter().use {
            it?.print(gson.toJson(gameState))
        }
    }

    @Synchronized
    fun loadState(): MutableList<GameState> {

        var inputStream: FileInputStream?
        kotlin.runCatching {
            inputStream = file?.inputStream()

            val list = mutableListOf<GameState>()
            inputStream?.let {
                val size = it.available()
                val buffer = ByteArray(size)
                it.read(buffer)
                it.close()
                val json = String(buffer)
                val state: List<GameState> = gson.fromJson(json, object : TypeToken<MutableList<GameState>>() {}.type)
                print(state)
                list.addAll(state)
            }
            return list
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
        val savedState = loadState()
        if (savedState.size >= savedCount) {
            savedState.remove(savedState.minBy { it.savedTime })
        }
        savedState.add(state)
        file?.printWriter().use {
            it?.print(gson.toJson(savedState))
        }
    }

    fun deleteState(gameId: Int) {
        val savedState = loadState()
        savedState.remove(savedState.firstOrNull { it.gameId == gameId })
        saveState(savedState)
    }

}

