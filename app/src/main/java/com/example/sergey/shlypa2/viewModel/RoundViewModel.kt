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

    private val dataProvider = DataProvider(application)
    private val soundManager = SoundManager(getApplication())

    val commandCallback: MutableLiveData<Command> = SingleLiveEvent()


    private val round = Game.getRound()
    private var roundFinished = false

    var roundDescription = round.description
    var roundRules = round.rules
    var roundName = round.name
    var roundImage = round.image

    val wordLiveData = MutableLiveData<Word>()
    //First value - answered, second - skipped
    val answeredCountLiveData = MutableLiveData<Pair<Int, Int>>()

    val timerLiveData = MutableLiveData<Int>()
    var timeLeft = Game.getSettings().time
    var timerStarted = true

    val handler = Handler()



    init {
        wordLiveData.value = round.getWord()
    }


    fun getPlayer() = round.getPlayer()

    fun answerWord(answer: Boolean) {
        round.answer(answer)

        answeredCountLiveData.value = round.getTurnAnswersCount()

        //play sound
        soundManager.play(if(answer)R.raw.correct else R.raw.wrong)

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
        if(!roundFinished) {
            Game.beginNextRound()
            roundFinished = true
        }

        if (Game.hasRound()) {
            saveGameState()
            commandCallback.value = Command.START_NEXT_ROUND
        } else {
            dataProvider.deleteState(Game.state.gameId)
            commandCallback.value = Command.SHOW_GAME_RESULTS
        }
    }

    fun startTurn() {
        timeLeft = Game.getSettings().time
        timerLiveData.value = timeLeft

        commandCallback.value = Command.START_TURN
    }

    private fun finishTurn() {
        //TODO save state of game here or in the finish round function
        timerStarted = false
        round.turnFinished = true

        commandCallback.value = Command.FINISH_TURN

        handler.removeCallbacksAndMessages(null)
    }

    fun getTurnResults(): List<Word> {
        return round.wordsAnsweredByPlayer
    }

    fun nextTurn() {
        saveGameState()

        round.turnFinished = false
        round.nextPlayer()
        if (round.getWord() != null) {
            answeredCountLiveData.value = Pair(0, 0)
            commandCallback.value = Command.GET_READY
        } else {
            commandCallback.value = Command.SHOW_ROUND_RESULTS
        }

        //Just for debug
        round.printHatContaining()
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

    fun saveGameState() {
        dataProvider.insertState(Game.state)
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
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