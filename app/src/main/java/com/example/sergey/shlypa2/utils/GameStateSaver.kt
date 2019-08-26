package com.example.sergey.shlypa2.utils

import com.example.sergey.shlypa2.game.GameState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class GameStateSaver {

    private val fileName = "gameState.txt"
    private val gson = Gson()
    private val file = File(fileName)
    private val inputStream = file.inputStream()
    private val savedCount = 5

    private fun saveState(gameState: List<GameState>) {

        file.writeText(gson.toJson(gameState))

    }

    fun loadState():MutableList<GameState>{
        val text = inputStream.bufferedReader().use { it.readText() }
        return gson.fromJson(text, object :TypeToken<MutableList<GameState>>(){}.type)
    }

    fun getLastSavedState():GameState? =  loadState().maxBy{ it.savedTime }

    fun insertState(state:GameState){
       val  savedState = loadState()
        if (savedState.size>=savedCount){
            savedState.remove(savedState.minBy { it.savedTime })
        }
        savedState.add(state)
        saveState(savedState)
    }

    fun deleteState(gameId:Int){
        val savedState = loadState()
        savedState.remove(savedState.firstOrNull { it.gameId == gameId })
        saveState(savedState)
    }

}

