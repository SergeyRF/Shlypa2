package com.example.sergey.shlypa2

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by sergey on 4/1/18.
 */
class StateViewModel : ViewModel() {
    private val timeLiveData = MutableLiveData<Int>()
    private val commandLiveData = MutableLiveData<Int>()
    private val wordsLiveData = MutableLiveData<Int>()
    private val teamCount = MutableLiveData<Int>()
    private val maxCount = MutableLiveData<Int>()
    private val hatWords = MutableLiveData<List<Word>>()
    init {
        updateStateData()
    }




    fun addWordLD(s: String) {
        Game.addWord(Word(s))
        updateStateData()
    }

    fun getWordPlayer(): LiveData<List<Word>> = hatWords


    fun getTimeLD(): LiveData<Int> = timeLiveData
    fun minusTimeLD(){
        if (Game.time>Game.miniTime) {
            Game.time-=10
        }
        updateStateData()
    }
    fun plusTimeLD(){
        Game.time+=10
        updateStateData()
    }


    fun getCommandLD(): LiveData<Int> =commandLiveData


    fun getMinusCommLD() {
        if (Game.command != Game.minimalComma) {
            Game.command--
        }
        updateStateData()
    }

    fun getPlusCommLD() {
        if (Game.command != Game.maxiCommand()) {
            Game.command++
        }
        updateStateData()
    }

    fun getCommandMinLD(): LiveData<Int> = teamCount
    fun getCommandMaxLD(): LiveData<Int> = maxCount
    fun getWordsLD(): LiveData<Int> = wordsLiveData
    fun minusWord(){
        if (Game.words>Game.miniWords){
            Game.words--
        }
        updateStateData()
    }
    fun plusWord(){
        Game.words++
        updateStateData()
    }



    fun updateStateData() {
        timeLiveData.value = Game.time
        commandLiveData.value = Game.command
        wordsLiveData.value = Game.words
        teamCount.value = Game.minimalComma
        maxCount.value = Game.maxiCommand()
        hatWords.value = Game.getWords()
    }
}
