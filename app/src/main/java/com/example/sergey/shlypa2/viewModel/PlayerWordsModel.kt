package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import timber.log.Timber
import java.util.*

/**
 * Created by sergey on 4/3/18.
 */
class PlayerWordsModel(application: Application) : AndroidViewModel(application) {

    private val wordsLiveData = MutableLiveData<List<Word>>()
    private val playerLivaData = MutableLiveData<Player>()

    val inputFinishCallBack = MutableLiveData<Boolean>()

    val needWord = MutableLiveData<Boolean>()

    private var pos: Int = 0
    private var words: MutableList<Word> = mutableListOf()

    private var db = DataProvider(application)
    private var randomWords: ArrayDeque<Word> = ArrayDeque()

    init {
        updateData()
    }

    fun nextPlayer() {
        for (word in words) {
            Game.addWord(word)
        }

        words.clear()
        pos++

        if (pos < Game.getPlayers().size) {
            updateData()
        } else {
            pos = 0
            inputFinishCallBack.value = true
        }
    }

    fun needWord(): Boolean {
        Timber.d("words size ${words.size} game settings words ${Game.getSettings().word}")
        return words.size < Game.getSettings().word
    }

    fun randomWord():Boolean = Game.getSettings().autoAddWords

    fun getPlayerLiveData(): LiveData<Player> {
        return playerLivaData
    }

    fun getWordsLiveData(): LiveData<List<Word>> = wordsLiveData

    fun addWord(s: String) {
        words.add(Word(s))
        updateData()
    }

    fun fillWithRandomWords() {
        val needWordsCount = Game.getSettings().word - words.size

        if (randomWords.size < needWordsCount) loadRandomWords()

        Timber.d("queue size after loading = ${randomWords.size}")
        for (i in 0 until needWordsCount)
            words.add(randomWords.poll())

        updateData()
    }


    private fun loadRandomWords() {
        var dbWords = db.getRandomWords(100, Game.getSettings().dificult)
        for (w in dbWords) Timber.d("$w")

        var unicWords: List<Word> = dbWords.filter { !Game.getWords().contains(it) && !words.contains(it) }
        Timber.d("unic words size ${unicWords.size}")

        randomWords.addAll(unicWords)
    }
    fun deleteWord(word:Word){
        words.remove(word)
        updateData()
    }
    fun renameWord(word:Word){
        val randomez:List<Word> = randomWords.filter { !words.contains(it) }
        words[words.indexOf(word)].word = randomez[0].word
        updateData()
    }

    private fun updateData() {
        wordsLiveData.value = words
        //todo index of bound exception here
        playerLivaData.value = Game.getPlayers()[pos]
        needWord.value = needWord()
    }
}