package com.example.sergey.shlypa2.db

import android.content.Context
import com.example.sergey.shlypa2.beans.Contract
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.StateRepresent
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.Dificult
import com.example.sergey.shlypa2.game.GameState
import com.google.gson.GsonBuilder

/**
 * Created by alex on 4/10/18.
 */
class DataProvider(val context: Context) {
    val db = DataBase.getInstance(context)
    val gson = GsonBuilder().setPrettyPrinting().create()

    fun getPlayers(type : Contract.PlayerType = Contract.PlayerType.STANDARD) : List<Player>
            = db.playersDao().getPlayersByType(type)


    fun getPlayer(id : Long) = db.playersDao().getPlayerById(id)

    fun insertPlayer(player : Player) : Long {
        return db.playersDao().insertPlayer(player)
    }

    fun getRandomWords(wordsLimit : Int, dificulty : Dificult) : List<Word> {
        val type = when(dificulty) {
            Dificult.EASY -> Contract.WordType.EASY
            Dificult.MEDIUM -> Contract.WordType.MEDIUM
            Dificult.HARD -> Contract.WordType.HARD
            Dificult.VERY_HARD -> Contract.WordType.VERY_HARD
        }

        return db.wordDao().getRandomWords(wordsLimit, type)
    }

    fun getSavedStates() : List<GameState> {
        val savedList = db.stateDao().getAllStates()

        val gson = GsonBuilder().create()
        return savedList.map { gson.fromJson(it.state, GameState::class.java) }
    }

    fun insertState(state : GameState) {
        val represent = StateRepresent(0, state.gameId, System.currentTimeMillis(), gson.toJson(state))
        db.stateDao().insertState(represent)
    }
}