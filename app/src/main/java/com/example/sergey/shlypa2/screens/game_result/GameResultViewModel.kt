package com.example.sergey.shlypa2.screens.game_result

import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.SoundManager
import com.example.sergey.shlypa2.utils.Sounds
import com.example.sergey.shlypa2.utils.coroutines.CoroutineViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameResultViewModel(
        private val dispatchers: DispatchersProvider,
        private val soundManager: SoundManager) : CoroutineViewModel(dispatchers.uiDispatcher) {

    val teamsLiveData = MutableLiveData<List<TeamWithScores>>()
    val winnerNameLiveData = MutableLiveData<String>()
    val commandLiveData = SingleLiveEvent<Command>()

    init {
        calculateResults()
    }

    fun onActivityCreated() {
        soundManager.playSound(Sounds.FANFAIR)
        launch {
            delay(3000)
            commandLiveData.value = Command.RUN_ANIMATION
        }
    }

    fun onRepeatGameClicked() {
        Game.repeatGame()
        commandLiveData.value = Command.START_GAME_SETTINGS
    }

    fun onFinishGameClicked() {
        commandLiveData.value = Command.START_MAIN_ACTIVITY
    }

    private fun calculateResults() {
        launch {
            val results = withContext(dispatchers.ioDispatcher) {
                val gameResults = Game
                        .getGameResults()
                        .sortedByDescending { it.getScores() }

                gameResults to gameResults.firstOrNull()?.team?.name
            }

            teamsLiveData.value = results.first
            winnerNameLiveData.value = results.second ?: "Unknown"
        }
    }

    enum class Command {
        RUN_ANIMATION,
        START_GAME_SETTINGS,
        START_MAIN_ACTIVITY
    }
}