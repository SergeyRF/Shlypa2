package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.Game.state
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.Round
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.utils.PreferenceHelper
import com.example.sergey.shlypa2.utils.PreferenceHelper.get
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.SoundManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber


/**
 * Created by alex on 4/3/18.
 */
class RoundViewModel(application: Application) : AndroidViewModel(application) {

    private val dataProvider = DataProvider(application)
    private var soundManager: SoundManager? = null

    val commandCallback: MutableLiveData<Command> = SingleLiveEvent()

    private lateinit var round: Round
    val roundLiveData = MutableLiveData<Round>()
    val rounResultLiveData = MutableLiveData<List<TeamWithScores>>()

    val wordLiveData = MutableLiveData<Word>()
    //First value - answered, second - skipped
    val answeredCountLiveData = MutableLiveData<Pair<Int, Int>>()

    val timerLiveData = MutableLiveData<Int>()
    var timeLeft = Game.getSettings().time
    var timerStarted = true

    private var roundFinished = false
    private var turnFinished = false

    val handler = Handler()

    init {
        val preference = PreferenceHelper.defaultPrefs(application)
        val soundsEnabledPref: Boolean = preference[Constants.SOUND_ON_PREF, true] ?: true
        if (soundsEnabledPref) {
            soundManager = SoundManager(application)
        }

        val gameRound = Game.getRound()

        when {
            Game.state.needToRestore -> loadGameState(Game.state)

            gameRound != null -> {
                round = gameRound
                roundLiveData.value = round
                wordLiveData.value = round.getWord()
            }

            else -> loadLastSaved()
        }
    }

    private fun loadLastSaved() {
        doAsync {
            val state = dataProvider.getLastSavedState()

            uiThread {
                if (state != null && Game.state.currentRound != null) {
                    loadGameState(Game.state)
                } else {
                    Timber.e(RuntimeException("Can't load game state"))
                    //Just for avoiding possible errors
                    round = Round(mutableListOf())
                    commandCallback.value = Command.EXIT
                }
            }
        }
    }

    fun loadHintTeam() {
        commandCallback.value = Command.SHOW_HINT_TEAM_TABLE
    }

    //todo add exception throwing
    private fun loadGameState(state: GameState) {
        state.needToRestore = false
        Game.state = state

        round = state.currentRound!!

        //Finish round if there's no word
        val word = round.getWord()
        if (word != null) {
            wordLiveData.value = word
        } else {
            //todo move to background thread
            rounResultLiveData.value = Game.getRoundResults()
            commandCallback.value = Command.SHOW_ROUND_RESULTS
            return
        }

        if (round.turnFinished) {
            commandCallback.value = Command.FINISH_TURN
        } else {
            commandCallback.value = Command.GET_READY
        }
    }

    fun getPlayer() = round.getPlayer()
    fun getTeam() = round.currentTeam.name

    fun answerWord(answer: Boolean) {
        round.answer(answer)

        answeredCountLiveData.value = round.getTurnAnswersCount()

        //play sound
        soundManager?.play(if (answer) R.raw.correct else R.raw.wrong)

        val word = round.getWord()
        if (word != null) {
            wordLiveData.value = word
        } else {
            finishTurn()
        }
    }

    fun beginRound() {
        showAds()
        commandCallback.value = Command.GET_READY
    }

    fun finishRound() {
        if (!roundFinished) {
            Game.beginNextRound()
            roundFinished = true
        }

        if (Game.hasRound()) {
            saveGameState()
            commandCallback.value = Command.START_NEXT_ROUND
        } else {
            val job = doAsync {
                dataProvider.deleteState(Game.state.gameId)
                uiThread { commandCallback.value = Command.SHOW_GAME_RESULTS }
            }
        }
    }

    private fun showAds() {
        commandCallback.value = Command.SHOW_INTERSTITIAL_ADS
    }

    fun startTurn() {
        timeLeft = Game.getSettings().time
        wordLiveData.value = round.getWord()
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
            answeredCountLiveData.value = 0 to 0
            commandCallback.value = Command.GET_READY
        } else {
            rounResultLiveData.value = Game.getRoundResults()
            showAds()
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
        doAsync {
            dataProvider.insertState(Game.state)
        }
    }

    fun portionClear() {
        Game.portionClear()
    }

    override fun onCleared() {
        super.onCleared()
        soundManager?.release()
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            timeLeft--
            if (timeLeft >= 0) {
                if (timeLeft == 10) {
                    soundManager?.play(R.raw.time_warn, 0.5f, 0.5f)
                }
                if (timeLeft == 1) {
                    soundManager?.play(R.raw.time_over, 0.5f, 0.5f)
                }
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
        SHOW_GAME_RESULTS,
        SHOW_HINT_TEAM_TABLE,
        SHOW_INTERSTITIAL_ADS,
        EXIT
    }

    fun loadCurrrentBal(): List<TeamWithScores> {
        val g = Game.getGameResults()
        val f = round.results

        g.forEach {
            val map = it.scoresMap
            it.team.players.forEach {
                var scores: Int = f[it.id] ?: 0
                scores += map[it.id] ?: 0
                map[it.id] = scores
            }

        }

        return g
    }

}