package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.SoundManager
import timber.log.Timber


/**
 * Created by alex on 4/3/18.
 */
class RoundViewModel(application: Application) : AndroidViewModel(application) {

    val dataProvider = DataProvider(application)

    val commandCallback: MutableLiveData<Command> = SingleLiveEvent()

    val round = Game.getRound()

    var roundDescription = round.description
    var roundRules = round.rules

    val wordLiveData = MutableLiveData<Word>()

    val timerLiveData = MutableLiveData<Int>()
    var timeLeft = Game.getSettings().time
    var timerStarted = true

    val handler = Handler()

    val soundManager = SoundManager(getApplication())

    init {
        wordLiveData.value = round.getWord()
        soundManager.preload(SoundManager.Sound.CORRECT, SoundManager.Sound.WRONG)
    }


    fun getPlayer() = round.getPlayer()

    fun answerWord(answer: Boolean) {
        round.answer(answer)

        //play sound
        soundManager.play(if(answer)SoundManager.Sound.CORRECT else SoundManager.Sound.WRONG)

        val word = round.getWord()
        if (word != null) {
            wordLiveData.value = word
        } else {
            finishTurn()
        }
    }

    fun beginRound() {
        commandCallback.value = Command.GET_READY
    }

    fun finishRound() {
        Game.beginNextRound()

        if (Game.hasRound()) {
            dataProvider.insertState(Game.state)
            commandCallback.value = Command.START_NEXT_ROUND
        } else {
            dataProvider.deleteState(Game.state.gameId)
            commandCallback.value = Command.SHOW_GAME_RESULTS
        }
    }

    fun startTurn() {
        commandCallback.value = Command.START_TURN
    }

    private fun finishTurn() {
        //TODO save state of game here or in the finish round function
        timerStarted = false

        commandCallback.value = Command.FINISH_TURN

        handler.removeCallbacksAndMessages(null)
        timeLeft = Game.getSettings().time
        timerLiveData.value = timeLeft
    }

    fun getTurnResults(): List<Word> {
        return round.wordsAnsweredByPlayer
    }

    fun nextTurn() {
        dataProvider.insertState(Game.state)

        round.nextPlayer()
        if (round.getWord() != null) {
            commandCallback.value = Command.GET_READY
        } else {
            commandCallback.value = Command.SHOW_ROUND_RESULTS
        }
    }

    fun startTimer() {
        timerStarted = true
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed(timerRunnable, 1000)
    }

    fun pauseTimer() {
        timerStarted = false
        handler.removeCallbacksAndMessages(null)
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            Timber.d("Thread in handler ${Thread.currentThread().name}")
            timeLeft--
            if (timeLeft >= 0) {
                timerLiveData.value = timeLeft
            } else {
                finishTurn()
            }

            if (timerStarted) handler.postDelayed(this, 1000)
        }
    }

    enum class Command {
        GET_READY,
        START_TURN,
        FINISH_TURN,
        SHOW_ROUND_RESULTS,
        START_NEXT_ROUND,
        SHOW_GAME_RESULTS
    }
}