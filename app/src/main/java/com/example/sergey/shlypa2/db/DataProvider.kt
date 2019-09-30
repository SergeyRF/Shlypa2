package com.example.sergey.shlypa2.db

import android.content.Context
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Lang
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.PlayerType
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.GameStateSaver
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


/**
 * Created by alex on 4/10/18.
 */
class DataProvider(
        db: DataBase,
        private val gson: Gson,
        private val context: Context,
        private val gameStateSaver:GameStateSaver
) {

    private val playersDao = db.playersDao()
    private val wordsDao = db.wordDao()
    private val stateDao = db.stateDao()
    private val typesDao = db.typesDap()

    private val locale: String = Locale.getDefault().language.toLowerCase()

    val teamNames by lazy { context.resources.getStringArray(R.array.teams) }

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

    fun getPlayersUser() = playersDao.getPlayersByType(PlayerType.USER)


    fun getPlayer(id: Long) = playersDao.getPlayerById(id)

    fun insertPlayer(player: Player): Long {
        return playersDao.insertPlayer(player)
    }

    fun deletePlayers(players:List<Player>) = playersDao.deletePlayers(players)
    fun deletePlayer(player: Player) = playersDao.deletePlayers(player)

    fun getRandomWords(wordsLimit: Int, typeId: Long): List<Word> {
        return wordsDao.getRandomWords(wordsLimit, typeId)
    }

    fun getSavedStates(): List<GameState> {
        return gameStateSaver.loadState()
    }

    fun getLastSavedState(): GameState? {
        return gameStateSaver.getLastSavedState()
    }

    fun insertState(state: GameState) {
        gameStateSaver.insertState(state)
    }

    fun deleteState(gameId: Int) {
        //stateDao.deleteState(gameId)
        gameStateSaver.deleteState(gameId)
    }

    fun getListOfAvatars(): List<String> {
        val jsonList = Functions.readJsonFromAssets(context, "files.json")

        val fileNameList: List<String> = gson.fromJson(jsonList, object : TypeToken<List<String>>() {}.type)
        return fileNameList
    }

    fun getTypes() = typesDao.getTypesForLang(lang)

    // Migrations
    fun copyLegacyStates() {
        val states = stateDao.getAllStates().map {
            gson.fromJson(it.state, GameState::class.java)
        }

        if(states.isNotEmpty()) {
            gameStateSaver.replaceStates(states)
        }
    }
}