package com.example.sergey.shlypa2.db

import android.content.Context
import com.example.sergey.shlypa2.beans.Lang
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.StateRepresent
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.PlayerType
import com.example.sergey.shlypa2.utils.Functions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.standalone.KoinComponent
import timber.log.Timber
import java.util.*


/**
 * Created by alex on 4/10/18.
 */
class DataProvider(
        db: DataBase,
        private val gson: Gson,
        private val context: Context
) {

    private val playersDao = db.playersDao()
    private val wordsDao = db.wordDao()
    private val stateDao = db.stateDao()
    private val typesDao = db.typesDap()

    private val locale: String = Locale.getDefault().language.toLowerCase()

    //we only need to use locales for which we have a translate
    private val usefullLocale = when (locale) {
        "ru" -> locale
        else -> "en"
    }

    private val lang = when(locale) {
        "ru" -> Lang.RU
        else -> Lang.EN
    }

    fun getPlayers(type: PlayerType = PlayerType.STANDARD): List<Player> {
        return playersDao.getPlayersByType(type, locale = usefullLocale)
    }


    fun getPlayer(id: Long) = playersDao.getPlayerById(id)

    fun insertPlayer(player: Player): Long {
        return playersDao.insertPlayer(player)
    }

    fun getRandomWords(wordsLimit: Int, typeId: Long): List<Word> {
        return wordsDao.getRandomWords(wordsLimit, typeId)
    }

    fun getSavedStates(): List<GameState> {
        var savedList = stateDao.getAllStates()

        //keep only 5 states
        if (savedList.size > 5) {
            val sortesList = savedList.sortedByDescending { it.time }
            sortesList.subList(4, sortesList.size - 1)
                    .forEach { stateDao.deleteState(it.gameId) }

            savedList = stateDao.getAllStates()
        }

        return savedList.map { gson.fromJson(it.state, GameState::class.java) }
    }

    fun getLastSavedState(): GameState? {
        val savedList = stateDao.getAllStates()
        val lastSaved = savedList.maxBy { it.time }

        return lastSaved?.let { gson.fromJson(lastSaved.state, GameState::class.java) }
    }

    fun insertState(state: GameState) {
        val represent = StateRepresent(0, state.gameId, System.currentTimeMillis(), gson.toJson(state))
        Timber.d(represent.state)
        stateDao.insertState(represent)
    }

    fun deleteState(gameId: Int) {
        stateDao.deleteState(gameId)
    }

    fun getListOfAvatars(): List<String> {
        val jsonList = Functions.readJsonFromAssets(context, "files.json")

        val fileNameList: List<String> = gson.fromJson(jsonList, object : TypeToken<List<String>>() {}.type)
        return fileNameList
    }

    fun getTypes() = typesDao.getTypesForLang(lang)
}