package com.example.sergey.shlypa2.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.coroutines.CoroutineAndroidViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by alex on 4/17/18.
 */
class WelcomeViewModel(application: Application,
                       private val dispatchers: DispatchersProvider,
                       private val dataProvider: DataProvider) : CoroutineAndroidViewModel(dispatchers.uiDispatcher, application) {


    val commandsCallBack : MutableLiveData<Commands> = SingleLiveEvent()
    val savedStates = MutableLiveData<List<GameState>>()

    fun loadSavedStates() {
      launch(dispatchers.ioDispatcher) {
          savedStates.postValue(dataProvider.getSavedStates())
      }
    }

    fun newGame() {
        Game.clear()
        Game.state.gameId = Functions.getGameId(getApplication())
        commandsCallBack.value = Commands.NEW_GAME
    }

    fun savedGames() {
        commandsCallBack.value = Commands.SAVED_GAMES
    }

    fun rules() {
        commandsCallBack.value = Commands.RULES
    }

    enum class Commands {
        NEW_GAME, SAVED_GAMES, RULES
    }
}