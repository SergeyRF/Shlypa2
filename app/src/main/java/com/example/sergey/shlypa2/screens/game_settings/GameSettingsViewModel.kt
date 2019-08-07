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

    private val settingsProvider = SettingsProviderImpl(application)

    private var settings = settingsProvider.getSettings()

    val waitLoadingTypes = MutableLiveData<Boolean>()

    private lateinit var typesList: List<Type>
    private lateinit var typeSelected: Type

    val startNextActivity = SingleLiveEvent<StartActivity>()

    init {
        loadTypes()
    }

    fun getTypesList() = typesList.toList()
    fun getTypeSelected() = typeSelected

    fun getTime(): Int = settings.time
    fun setTime(i: Int) {
        settings.time = i
    }

    fun getWordsCount(): Int = settings.word
    fun setWordsLD(i: Int) {
        settings.word = i
    }

    fun getDifficulty(): Long = settings.typeId
    fun setDifficulty(wordType: Type) {
        Timber.d("$wordType")
        settings.typeId = wordType.id
        settings.typeName = wordType.name
    }

    fun getAllowRandom(): Boolean = settings.allowRandomWords
    fun setAllowRandom(b: Boolean) {
        settings.allowRandomWords = b
    }

    fun getPenaltyInclude(): Boolean = settings.penaltyInclude
    fun setPenaltyInclude(b: Boolean) {
        settings.penaltyInclude = b
    }

    fun getPenaltyPoint(): Int = settings.penaltyPoint
    fun setPenaltyPoint(i: Int) {
        settings.penaltyPoint = i
    }

    fun getAllWorldRandom() = settings.all_word_random
    fun setAllWorldRandom(b: Boolean) {
        settings.all_word_random = b
    }

    fun onFinish() {
        savedSettings()
        anal.gameStarted(
                Game.getSettings().allowRandomWords,
                Game.getSettings().typeName,
                true)
        startNextActivity.value = StartActivity.WORD_IN
    }

    fun savedSettings() {
        Game.setSettings(settings)
        settingsProvider.writeSettings(settings)
    }

    private fun loadTypes() {
        launch(dispatchers.uiDispatcher) {
            val types = withContext(dispatchers.ioDispatcher) {
                dataProvider.getTypes()
            }
            typesList = types

            val typeId = settings.typeId

            typeSelected = types.first { it.id == typeId }

            waitLoadingTypes.value = true

        }
    }

    enum class StartActivity {
        WORD_IN,
        START_GAME
    }
}
