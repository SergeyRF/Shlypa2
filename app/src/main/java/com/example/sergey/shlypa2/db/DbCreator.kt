package com.example.sergey.shlypa2.db

import android.content.Context
import com.example.sergey.shlypa2.beans.*
import com.example.sergey.shlypa2.utils.Functions
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber

/**
 * Created by alex on 4/10/18.
 */
object DbCreator {

    fun createPlayers(dataBase: DataBase, context: Context) {
        val players = dataBase.playersDao().getAllPlayers()
        if (players.isEmpty()) {
            val somePlayersJson = Functions.readJsonFromAssets(context, "players.json")
            val gson = GsonBuilder().create()
            val somePlayersList: List<SomePlayer> = gson.fromJson(somePlayersJson,
                    object : TypeToken<List<SomePlayer>>() {}.type)

            for (somePlayer in somePlayersList) {
                dataBase.playersDao().insertPlayer(
                        Player(name = somePlayer.name,
                                locale = somePlayer.locale,
                                avatar = somePlayer.avatar,
                                type = PlayerType.STANDARD))
            }
        }
    }

    fun createWords(dataBase: DataBase, context: Context) {
        loadWordsFromAsset("words/wordsRu.json", Lang.RU, dataBase, context)
        loadWordsFromAsset("words/wordsEn.json", Lang.EN, dataBase, context)
    }

    private fun loadWordsFromAsset(fileName: String, lang: Lang, dataBase: DataBase, context: Context) {
        val wordsJsonFile = context.assets.open(fileName)
        val jsonString = String(wordsJsonFile.readBytes())
        val gson = GsonBuilder().create()
        val dictionaryList: List<Dictionary> = gson.fromJson(jsonString,
                object : TypeToken<List<Dictionary>>() {}.type)

        Timber.d("TESTING $dictionaryList")

        dictionaryList.forEach {
            val typeId = dataBase.typesDap().insertType(Type(0, it.name, lang))
            it.words.forEach { word ->
                dataBase.wordDao().insertWord(
                        Word(word, 0, typeId, 0)
                )
            }
        }
    }
}

data class SomePlayer(val name: String, val locale: String, val avatar: String)