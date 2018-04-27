package com.example.sergey.shlypa2.db

import android.content.Context
import android.os.Environment
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.PlayerType
import com.example.sergey.shlypa2.game.WordType
import com.example.sergey.shlypa2.utils.Functions
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.InputStreamReader

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
        val wordsList = dataBase.wordDao().getAllWords()
        if (wordsList.isNotEmpty()) return


        addWordsFromStream(context, dataBase, "words/easyRu", WordType.EASY, "ru")
        addWordsFromStream(context, dataBase,"words/normalRu", WordType.MEDIUM, "ru")
        addWordsFromStream(context, dataBase, "words/hardRu", WordType.HARD, "ru")
        addWordsFromStream(context, dataBase, "words/veryhardRu", WordType.VERY_HARD, "ru")

    }

    private fun addWordsFromStream(context: Context, dataBase: DataBase, fileName: String,
                                   type: WordType, lang: String) {
        val wordStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(wordStream))

        var line: String? = reader.readLine()

        while (line != null) {
            Timber.d("load words from asset $line")
            dataBase.wordDao().insertWord(Word(line, type = type, lang = lang))
            line = reader.readLine()
        }
    }

    fun sortAndWriteWords(context: Context) {
        var wordStream = context.assets.open("words/easyRu")
        var reader = BufferedReader(InputStreamReader(wordStream))
        sort(reader, "easyRu")

        wordStream = context.assets.open("words/normalRu")
        reader = BufferedReader(InputStreamReader(wordStream))
        sort(reader, "normalRu")

        wordStream = context.assets.open("words/hardRu")
        reader = BufferedReader(InputStreamReader(wordStream))
        sort(reader, "hardRu")

        wordStream = context.assets.open("words/veryhardRu")
        reader = BufferedReader(InputStreamReader(wordStream))
        sort(reader, "veryhardRu")
    }

    private fun sort(reader: BufferedReader, name: String) {
        val list = mutableListOf<String>()
        var line: String? = reader.readLine()

        while (line != null) {
            list.add("$line\n")
            line = reader.readLine()
        }

        list.sort()

        val fileWriter = FileWriter("${Environment.getExternalStorageDirectory()}/$name")
        list.forEach { fileWriter.write(it) }

        fileWriter.close()
    }

    fun loadFileList(context: Context) {
        val asset = context.assets.list("player_avatars")
        Timber.d("list if files")
        for (file in asset) {
            Timber.d(file)
        }

        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonList = gson.toJson(asset)

        Timber.d(jsonList)

        val fileWriter = FileWriter("${Environment.getExternalStorageDirectory()}/files.json")
        fileWriter.write(jsonList)
        fileWriter.close()

        val somePlayersList: MutableList<SomePlayer> = mutableListOf()

        for (i in 0 until asset.size) {
            somePlayersList.add(SomePlayer("Player $i en", "en", asset[i]))
        }

        for (i in 0 until asset.size) {
            somePlayersList.add(SomePlayer("Player $i ru", "ru", asset[i]))
        }

        val playersJson = gson.toJson(somePlayersList)
        val write = FileWriter("${Environment.getExternalStorageDirectory()}/players.json")
        write.write(playersJson)
        write.close()
    }
}

data class SomePlayer(val name: String, val locale: String, val avatar: String)