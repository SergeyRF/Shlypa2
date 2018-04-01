package com.example.sergey.shlypa2

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by sergey on 4/1/18.
 */
class StateViewModel: ViewModel(){
    private val timeLiveData = MutableLiveData<Int>()
    private val commandLivaData = MutableLiveData<Int>()
    private val wordsLiveData= MutableLiveData<Int>()
    private val teamCount = MutableLiveData<Int>()
    private val maxCount = MutableLiveData<Int>()

    fun getTimeLD():LiveData<Int> = timeLiveData
    fun setTimeLD(i:Int) {
        Game.time = i
        updateStateData()
    }
    fun getCommandLD():LiveData<Int> = commandLivaData
    fun setCommandLD(i:Int){
        Game.command = i
        updateStateData()
    }

    fun getCommandMinlLD():LiveData<Int> = teamCount

    fun getWorldsLD():LiveData<Int> = wordsLiveData
    fun setWorlds(i:Int){
        Game.words= i
        updateStateData()
    }
    fun getCommandMaxLD():LiveData<Int> = maxCount


    fun updateStateData(){
        timeLiveData.value = Game.time
        commandLivaData.value = Game.command
        wordsLiveData.value = Game.words
        teamCount.value = Game.minimalComma
        maxCount.value = Game.maxiCommand()
    }
}