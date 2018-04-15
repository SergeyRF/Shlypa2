package com.example.sergey.shlypa2.db

import android.content.Context
import com.example.sergey.shlypa2.beans.Contract
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Created by alex on 4/10/18.
 */
object DbCreator {

    fun createPlayers(dataBase: DataBase) {
        val players = dataBase.playersDao().getAllPlayers()
        if(players.isEmpty()) {
            val names = arrayOf("Ashot", "Vazgen", "Zurab", "Gjamshoot", "Gogi",
                    "Givi", "Samir", "Magamet", "Azamat", "Ahmad")

            for(name in names) {
                dataBase.playersDao().insertPlayer(Player(name = name))
            }
        }


    }

    fun createWords(dataBase: DataBase, context: Context) {
        val wordsList = dataBase.wordDao().getAllWords()
        if(wordsList.isNotEmpty()) return

        val wordsStream = context.assets.open("words.txt")
        val wordsBufferedReader = BufferedReader(InputStreamReader(wordsStream))

        var line : String? = wordsBufferedReader.readLine()

        while(line != null) {
            Timber.d("load words from asset $line")
            dataBase.wordDao().insertWord(Word("$line easy", type = Contract.WordType.EASY))
            dataBase.wordDao().insertWord(Word("$line hard", type = Contract.WordType.HARD))
            dataBase.wordDao().insertWord(Word("$line med", type = Contract.WordType.MEDIUM))
            line = wordsBufferedReader.readLine()
        }
    }
}