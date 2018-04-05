package com.example.sergey.shlypa2.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.game.Game

/**
 * Created by sergey on 4/1/18.
 */
class StateViewModel : ViewModel() {
    private val timeLiveData = MutableLiveData<Int>()
    private val wordsLiveData = MutableLiveData<Int>()
    private val minCount = MutableLiveData<Int>()
    private val maxCount = MutableLiveData<Int>()

    init {
        updateStateData()
    }

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

    fun getCommandMinLD(): LiveData<Int> = minCount
    fun getCommandMaxLD(): LiveData<Int> = maxCount
    fun createTeams(count : Int) : Boolean{
        return if(Game.getPlayers().size >= count) {
            Game.createTeams(count)
            true
        } else false
    }

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
        wordsLiveData.value = Game.words
        minCount.value = Constants.MIN_TEAM_COUNT
        maxCount.value = Game.maxTeamsCount()

    }
}
