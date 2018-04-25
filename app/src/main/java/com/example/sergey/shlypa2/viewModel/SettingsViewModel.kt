package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.SettingsProviderImpl
import com.example.sergey.shlypa2.game.WordType
import timber.log.Timber

/**
 * Created by sergey on 4/1/18.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private var wordsCount = 0


    private var dificult: WordType

    private var allowRandom = false
    private var minusBal = false
    private var numberMinusBal = 1
    private var turnTime = 0

    private val settingsProvider = SettingsProviderImpl(application)

    private var settings = settingsProvider.getSettings()

    init {
        turnTime = settings.time
        wordsCount = settings.word
        allowRandom = settings.allowRandomWords
        dificult = settings.type
        minusBal = settings.minusBal
        numberMinusBal = settings.numberMinusBal
    }

    fun getTime(): Int = turnTime
    fun setTime(i: Int) {
        settings.time = i
    }

    fun getWordsCount(): Int = wordsCount
    fun setWordsLD(i: Int) {
        settings.word = i
    }

    fun getDifficulty(): WordType = dificult
    fun setDifficulty(d: WordType) {
        Timber.d("$d")
        settings.type = d
    }

    fun getAllowRandom(): Boolean = allowRandom
    fun setAllowRandom(b: Boolean) {
        settings.allowRandomWords = b
    }

    fun getMinusBal(): Boolean = minusBal
    fun setMinusBal(b: Boolean) {
        settings.minusBal = b
    }

    fun getnumberMinusBal(): Int = numberMinusBal
    fun setnumberMInusBal(i: Int) {
        settings.numberMinusBal = i
    }

    fun onFinish() {
        Game.setSettings(settings)
        settingsProvider.writeSettings(settings)
    }
}
