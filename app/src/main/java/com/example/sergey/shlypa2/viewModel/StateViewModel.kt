package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.SettingsProviderImpl

/**
 * Created by sergey on 4/1/18.
 */
class StateViewModel(application: Application) : AndroidViewModel(application) {
    private val timeLiveData = MutableLiveData<Int>()
    private val wordsLiveData = MutableLiveData<Int>()
    private val minCount = MutableLiveData<Int>()
    private val maxCount = MutableLiveData<Int>()

    val settingsProvider = SettingsProviderImpl(application)

    var settings = settingsProvider.getSettings()

    init {
        updateStateData()
    }

    fun getTimeLD(): LiveData<Int> = timeLiveData
    fun minusTimeLD(){
        if (settings.time> Constants.MIN_ROUND_TIME) {
            settings.time-=10
        }
        updateStateData()
    }
    fun plusTimeLD(){
        settings.time+=10
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
        if (settings.word> Constants.MIN_WORDS_COUNT){
            settings.word--
        }
        updateStateData()
    }
    fun plusWord(){
        settings.word++
        updateStateData()
    }

    fun updateStateData() {
        timeLiveData.value = settings.time
        wordsLiveData.value =settings.word
        minCount.value = Constants.MIN_TEAM_COUNT
        maxCount.value = Game.maxTeamsCount()

    }

    fun onFinish() {
        Game.settings = settings
        settingsProvider.writeSettings(settings)
    }
}
