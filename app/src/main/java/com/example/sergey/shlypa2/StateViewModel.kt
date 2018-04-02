package com.example.sergey.shlypa2

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by sergey on 4/1/18.
 */
class StateViewModel: ViewModel(){
    private val timeLiveData = MutableLiveData<Int>()
    private val commandLiveData = MutableLiveData<Int>()
    private val wordsLiveData= MutableLiveData<Int>()
    private val teamCount = MutableLiveData<Int>()
    private val maxCount = MutableLiveData<Int>()

    fun getTimeLD():LiveData<Int> = timeLiveData
    fun setTimeLD(i:Int) {
        Game.time = i
        updateStateData()
    }
    fun getCommandLD():LiveData<Int> {
        updateStateData()
        return commandLiveData
    }
    fun getMinusCommLD(){
        if(Game.command!= Game.minimalComma){
            Game.command--
        }
        updateStateData()
    }
    fun getPlusCommLD(){
        if (Game.command!=Game.maxiCommand()){
            Game.command++
        }
        updateStateData()
    }
    fun getCommandMinLD():LiveData<Int> = teamCount
    fun getCommandMaxLD():LiveData<Int> = maxCount
    fun getWorldsLD():LiveData<Int> = wordsLiveData
    fun setWorlds(i:Int){
        Game.words= i
        updateStateData()
    }



    fun updateStateData(){
        timeLiveData.value = Game.time
        commandLiveData.value = Game.command
        wordsLiveData.value = Game.words
        teamCount.value = Game.minimalComma
        maxCount.value = Game.maxiCommand()
    }
}