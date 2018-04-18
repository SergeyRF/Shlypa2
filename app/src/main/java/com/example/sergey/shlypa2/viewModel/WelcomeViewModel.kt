package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.SingleLiveEvent

/**
 * Created by alex on 4/17/18.
 */
class WelcomeViewModel(application: Application) : AndroidViewModel(application) {

    val dataProvider = DataProvider(application)

    val commandsCallBack : MutableLiveData<Commands> = SingleLiveEvent()

    fun getSavedStates() : List<GameState> {
        return dataProvider.getSavedStates()
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