package com.example.sergey.shlypa2.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.Game

import timber.log.Timber


/**
 * Created by alex on 4/3/18.
 */
class RoundViewModel : ViewModel() {

    val round = Game.getRound()

    var roundDescription = round.description
    var roundRules = round.rules

    val wordLiveData = MutableLiveData<Word>()
    val finishTurnCall = MutableLiveData<Boolean>()

    val timerLiveData = MutableLiveData<Int>()
    var timeLeft = Game.settings.time
    var timerStarted = true

    val handler = Handler()

    var roundFinished = false

    init {
        wordLiveData.value = round.getWord()
    }


    fun getPlayer() = round.getPlayer()

    fun answerWord(answer: Boolean) {
        round.answer(answer)
        val word = round.getWord()
        if (word != null) {
            wordLiveData.value = word
        } else {
            roundFinished = true
            finishTurnCall.value = true
        }
    }

    fun getTurnResults(): List<Word> {
        return round.wordsAnsweredByPlayer
    }

    fun finishTurn() {
        round.nextPlayer()
        timerStarted = false
        handler.removeCallbacksAndMessages(null)
        timeLeft = Game.settings.time
        timerLiveData.value = timeLeft
        finishTurnCall.value = false
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

    private val timerRunnable = object : Runnable {
        override fun run() {
            Timber.d("Thread in handler ${Thread.currentThread().name}")
            timeLeft--
            if (timeLeft >= 0) {
                timerLiveData.value = timeLeft
            } else {
                finishTurnCall.value = true
            }

            if (timerStarted) handler.postDelayed(this, 1000)
        }
    }
}