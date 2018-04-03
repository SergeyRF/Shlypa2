package com.example.sergey.shlypa2.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.Word

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
        if (Game.time> Constants.MIN_ROUND_TIME) {
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
        if (Game.teamsCount != Constants.MIN_TEAM_COUNT) {
            Game.teamsCount--
        }
        updateStateData()
    }

    fun getPlusCommLD() {
        if (Game.teamsCount != Game.maxTeamsCount()) {
            Game.teamsCount++
        }
        updateStateData()
    }

    fun getCommandMinLD(): LiveData<Int> = teamCount
    fun getCommandMaxLD(): LiveData<Int> = maxCount
    fun getWordsLD(): LiveData<Int> = wordsLiveData
    fun minusWord(){
        if (Game.words> Constants.MIN_WORDS_COUNT){
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
        commandLiveData.value = Game.teamsCount
        wordsLiveData.value = Game.words
        teamCount.value = Constants.MIN_TEAM_COUNT
        maxCount.value = Game.maxTeamsCount()
        hatWords.value = Game.getWords()
    }
}
