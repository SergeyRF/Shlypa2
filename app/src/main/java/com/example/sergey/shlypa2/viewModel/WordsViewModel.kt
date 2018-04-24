package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.beans.Contract
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.WordType
import timber.log.Timber
import java.util.*

/**
 * Created by sergey on 4/3/18.
 */
class WordsViewModel(application: Application) : AndroidViewModel(application) {

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


    fun needWordSize():Int =  Game.getSettings().word - words.size


    fun randomAllowed():Boolean = Game.getSettings().allowRandomWords

    fun getPlayerLiveData(): LiveData<Player> {
        return playerLivaData
    }

    fun getWordsLiveData(): LiveData<List<Word>> = wordsLiveData

    fun addWord(s: String) {
        words.add(Word(s))
        updateData()
    }
    fun reNameWord(word:Word){
        words[words.indexOf(word)].type = WordType.USER
        words[words.indexOf(word)].word=word.word
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
        var dbWords = db.getRandomWords(100, Game.getSettings().type)
        for (w in dbWords) Timber.d("$w")

        var unicWords: List<Word> = dbWords.filter { !Game.getWords().contains(it) && !words.contains(it) }
        Timber.d("unic words size ${unicWords.size}")

        randomWords.addAll(unicWords)
    }

    fun deleteWord(word:Word){
        words.remove(word)
        updateData()
    }

    fun newRandomWord(word:Word){
        if(randomWords.isEmpty()) loadRandomWords()

        words[words.indexOf(word)] = randomWords.poll()
        updateData()
    }

    private fun updateData() {
        wordsLiveData.value = words
        //todo index of bound exception here
        playerLivaData.value = Game.getPlayers()[pos]
        needWord.value = needWord()
    }
}