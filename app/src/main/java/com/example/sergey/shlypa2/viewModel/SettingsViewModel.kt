package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.game.Dificult
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.SettingsProviderImpl
import timber.log.Timber

/**
 * Created by sergey on 4/1/18.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val timeLiveData = MutableLiveData<Int>()
    private val wordsLiveData = MutableLiveData<Int>()
    private val minCount = MutableLiveData<Int>()
    private val maxCount = MutableLiveData<Int>()
    private val teemNeed = MutableLiveData<Int>()
    private val dificult = MutableLiveData<Dificult>()
    private val allowRandom = MutableLiveData<Boolean>()
    private val minusBal = MutableLiveData<Boolean>()
    private val numberMinusBal = MutableLiveData<Int>()

    val settingsProvider = SettingsProviderImpl(application)

    var settings = settingsProvider.getSettings()

    init {
        Timber.d("Dificulty ${settings.dificult}")
        updateStateData()
        if (teemNeed.value == null) teemNeed.value = Constants.MIN_TEAM_COUNT
    }

    fun getTimeLD(): LiveData<Int> = timeLiveData
    fun setTimeLD(i: Int) {
        settings.time = i
        updateStateData()
    }

    fun getCommandMaxLD(): LiveData<Int> = maxCount
    fun getTeemNeed(): LiveData<Int> = teemNeed
    fun setTeemNeed(i: Int) {
        teemNeed.value = i
    }

    fun createTeams(count: Int): Boolean {
        return if (Game.getPlayers().size >= count) {
            Game.createTeams(count)
            true
        } else false
    }

    fun getWordsLD(): LiveData<Int> = wordsLiveData
    fun setWordsLD(i: Int) {
        settings.word = i
        updateStateData()
    }

    fun getDificultLD(): LiveData<Dificult> = dificult
    fun setDificultLD(d: Dificult) {
        Timber.d("$d")
        settings.dificult = d
        updateStateData()
    }

    fun getAllowRandom(): LiveData<Boolean> = allowRandom
    fun setAllowRandom(b: Boolean) {
        settings.allowRandomWords = b
        updateStateData()
    }

    fun getMinusBal(): LiveData<Boolean> = minusBal
    fun setMinusBal(b: Boolean) {
        settings.minusBal = b
        updateStateData()
    }

    fun getnumberMinusBal(): LiveData<Int> = numberMinusBal
    fun setnumberMInusBal(i: Int) {
        settings.numberMinusBal = i
        updateStateData()
    }

    fun updateStateData() {
        timeLiveData.value = settings.time
        wordsLiveData.value = settings.word
        minCount.value = Constants.MIN_TEAM_COUNT
        maxCount.value = Game.maxTeamsCount()
        allowRandom.value = settings.allowRandomWords
        dificult.value = settings.dificult
        minusBal.value = settings.minusBal
        numberMinusBal.value = settings.numberMinusBal
    }


    fun onFinish() {
        Game.setSettings(settings)
        settingsProvider.writeSettings(settings)
    }
}
