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
import timber.log.Timber
import java.util.*


/**
 * Created by alex on 4/10/18.
 */
class DataProvider(val context: Context) {
    val db = DataBase.getInstance(context)
    val gson = GsonBuilder().setPrettyPrinting().create()

    val locale : String = Locale.getDefault().language.toLowerCase()

    //we only need to use locales for which we have a translate
    val usefullLocale = when(locale) {
        "ru" -> locale
        else -> "en"
    }

    fun getPlayers(type: PlayerType = PlayerType.STANDARD): List<Player> {
        return db.playersDao().getPlayersByType(type, locale = usefullLocale)
    }


    fun getPlayer(id: Long) = db.playersDao().getPlayerById(id)

    fun insertPlayer(player: Player): Long {
        return db.playersDao().insertPlayer(player)
    }

    fun getRandomWords(wordsLimit: Int, dificulty: WordType): List<Word> {
        return db.wordDao().getRandomWords(wordsLimit, dificulty, usefullLocale)
    }

    fun getSavedStates(): List<GameState> {
        var savedList = db.stateDao().getAllStates()

        //keep only 5 states
        if(savedList.size > 5) {
            val sortesList = savedList.sortedByDescending { it.time }
            sortesList.subList(4, sortesList.size - 1)
                    .forEach { db.stateDao().deleteState(it.gameId) }

            savedList = db.stateDao().getAllStates()
        }

        return savedList.map { gson.fromJson(it.state, GameState::class.java) }
    }

    fun getLastSavedState() : GameState? {
        val savedList = db.stateDao().getAllStates()
        val lastSaved = savedList.maxBy { it.time }

        return lastSaved?.let { gson.fromJson(lastSaved.state, GameState::class.java) }
    }

    fun insertState(state: GameState) {
        val represent = StateRepresent(0, state.gameId, System.currentTimeMillis(), gson.toJson(state))
        Timber.d(represent.state)
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