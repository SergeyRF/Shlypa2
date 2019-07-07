package com.example.sergey.shlypa2.screens.game_settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.beans.Type
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.SettingsProviderImpl
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.anal.AnalSender
import com.example.sergey.shlypa2.utils.coroutines.CoroutineAndroidViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Created by sergey on 4/1/18.
 */
class GameSettingsViewModel(application: Application,
                            private val dataProvider: DataProvider,
                            private val dispatchers: DispatchersProvider,
                            private val anal: AnalSender)
    : CoroutineAndroidViewModel(dispatchers.uiDispatcher, application) {

    private var wordsCount = 0
    private var wordsTypeId: Long = 0
    private var allowRandom = false
    private var minusBal = false
    private var numberMinusBal = 1
    private var turnTime = 0
    private var allWordRandom = false

    private val settingsProvider = SettingsProviderImpl(application)

    private var settings = settingsProvider.getSettings()

    val typesLiveData = MutableLiveData<List<Type>>()
    var selectedType: Type? = null

    val startNextActivity = SingleLiveEvent<StartActivity>()
    var flagStartGame: Boolean = false

    init {
        turnTime = settings.time
        wordsCount = settings.word
        allowRandom = settings.allowRandomWords
        wordsTypeId = settings.typeId
        minusBal = settings.minusBal
        numberMinusBal = settings.numberMinusBal
        allWordRandom = settings.all_word_random
        loadTypes()
    }

    fun getTime(): Int = turnTime
    fun setTime(i: Int) {
        settings.time = i
    }

    fun getWordsCount(): Int = wordsCount
    fun setWordsLD(i: Int) {
        settings.word = i
    }

    fun getDifficulty(): Long = wordsTypeId
    fun setDifficulty(wordType: Type) {
        Timber.d("$wordType")
        settings.typeId = wordType.id
        settings.typeName = wordType.name
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

    fun getAllWorldRandom() = allWordRandom
    fun setAllWorldRandom(b: Boolean) {
        settings.all_word_random = b
    }

    fun onFinish() {
        if (!flagStartGame) {
            flagStartGame = true
            Game.setSettings(settings)
            settingsProvider.writeSettings(settings)
            if (settings.all_word_random) {
                addRandomWords()
            } else {
                startNextActivity.value = StartActivity.WORLD_IN
                flagStartGame = false
            }
        }
    }

    private fun loadTypes() {
        launch {
            val types = withContext(dispatchers.ioDispatcher) {
                dataProvider.getTypes()
            }

            typesLiveData.value = types
        }
    }

    private fun addRandomWords() {
        GlobalScope.launch {
            val dbWords = dataProvider.getRandomWords(100, Game.getSettings().typeId)
            val wordNeeds = Game.getPlayers().size * settings.word
            Game.addWord(dbWords.subList(0, wordNeeds))
            Game.beginNextRound()
            startNextActivity.postValue(StartActivity.START_GAME)
            flagStartGame = false
            anal.gameStarted(
                    Game.getSettings().allowRandomWords,
                    Game.getSettings().typeName,
                    true)
        }

    }

    enum class StartActivity {
        WORLD_IN,
        START_GAME
    }
}
