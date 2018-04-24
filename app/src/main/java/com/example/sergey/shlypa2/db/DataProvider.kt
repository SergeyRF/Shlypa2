package com.example.sergey.shlypa2.db

import android.content.Context
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.StateRepresent
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.PlayerType
import com.example.sergey.shlypa2.game.WordType
import com.example.sergey.shlypa2.utils.Functions
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


/**
 * Created by alex on 4/10/18.
 */
class DataProvider(val context: Context) {
    val db = DataBase.getInstance(context)
    val gson = GsonBuilder().setPrettyPrinting().create()

    fun getPlayers(type: PlayerType = PlayerType.STANDARD): List<Player> = db.playersDao().getPlayersByType(type)


    fun getPlayer(id: Long) = db.playersDao().getPlayerById(id)

    fun insertPlayer(player: Player): Long {
        return db.playersDao().insertPlayer(player)
    }

    fun getRandomWords(wordsLimit: Int, dificulty: WordType): List<Word> {


        return db.wordDao().getRandomWords(wordsLimit, dificulty)
    }

    fun getSavedStates(): List<GameState> {
        val savedList = db.stateDao().getAllStates()

        val gson = GsonBuilder().create()
        return savedList.map { gson.fromJson(it.state, GameState::class.java) }
    }

    fun insertState(state: GameState) {
        val represent = StateRepresent(0, state.gameId, System.currentTimeMillis(), gson.toJson(state))
        db.stateDao().insertState(represent)
    }

    fun deleteState(gameId: Int) {
        db.stateDao().deleteState(gameId)
    }

    fun getListOfAvatars(): List<String> {
        val jsonList = Functions.readJsonFromAssets(context, "files.json")

        val fileNameList: List<String> = gson.fromJson(jsonList, object : TypeToken<List<String>>() {}.type)
        return fileNameList
    }
}