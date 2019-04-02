package com.example.sergey.shlypa2.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by alex on 4/17/18.
 */
class WelcomeViewModel(application: Application,
                       val dataProvider: DataProvider) : AndroidViewModel(application) {


    val commandsCallBack : MutableLiveData<Commands> = SingleLiveEvent()
    private val savedStates : MutableLiveData<List<GameState>> = MutableLiveData()

    fun getSavedStates() : LiveData<List<GameState>> {
        doAsync {
            val savedStatesList = dataProvider.getSavedStates()

            uiThread {
                savedStates.value = savedStatesList.sortedByDescending { it.savedTime }
            }
        }

        return savedStates
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