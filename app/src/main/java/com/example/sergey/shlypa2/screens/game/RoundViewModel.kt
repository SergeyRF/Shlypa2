package com.example.sergey.shlypa2.screens.game

import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.ads.AdsManager
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.Round
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.SoundManager
import com.example.sergey.shlypa2.utils.Sounds
import com.example.sergey.shlypa2.utils.anal.AnalSender
import com.example.sergey.shlypa2.utils.coroutines.CoroutineViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.collections.set


/**
 * Created by alex on 4/3/18.
 */
class RoundViewModel(
        private val dispatchers: DispatchersProvider,
        private val dataProvider: DataProvider,
        private val anal: AnalSender,
        private val soundManager: SoundManager,
        private val adsManager: AdsManager) : CoroutineViewModel(dispatchers.uiDispatcher) {


    val commandCallback: MutableLiveData<Command> = SingleLiveEvent()

    private lateinit var round: Round
    val roundLiveData = MutableLiveData<Round>()
    val rounResultLiveData = MutableLiveData<List<TeamWithScores>>()
    val scoresSheetLiveData = MutableLiveData<List<TeamWithScores>>()

    val nativeAdLiveData = SingleLiveEvent<UnifiedNativeAd>()

    val wordLiveData = MutableLiveData<Word>()
    //First value - answered, second - skipped
    val answeredCountLiveData = MutableLiveData<Pair<Int, Int>>()

    val timerStateLiveData = MutableLiveData<Boolean>()
    val timerLiveData = MutableLiveData<Int>()
    var timeLeft = Game.getSettings().time
    private var ticker: ReceiveChannel<Unit>? = null
    private var timerStarted = true

    private var roundFinished = false

    private var adsShowedTime = System.currentTimeMillis()
    private val interstitialDelay = adsManager.interstitialDelayMs
    val interstitialEnabled = adsManager.interstitialEnabled
    val nativeAdEnabled = adsManager.nativeBeforeTurnEnabled


    init {
        val gameRound = Game.getRound()

        when {
            Game.state.needToRestore -> {
                loadGameState(Game.state)
            }

            gameRound != null -> {
                round = gameRound
                roundLiveData.value = round
                wordLiveData.value = round.getWord()
            }

            else -> {
                loadLastSaved()
            }
        }

        if(nativeAdEnabled) commandCallback.value = Command.LOAD_NATIVE_AD
    }

    private fun loadLastSaved() {
        launch {
            val state = withContext(dispatchers.ioDispatcher) {
                dataProvider.getLastSavedState()
            }


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

    fun showScoresTable() {
        commandCallback.value = Command.SHOW_HINT_TEAM_TABLE
    }

    //todo add exception throwing
    private fun loadGameState(state: GameState) {
        state.needToRestore = false
        Game.state = state

        round = state.currentRound!!
        roundLiveData.value = round
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

    fun answerWord(answer: Boolean) {
        round.answer(answer)

        answeredCountLiveData.value = round.getTurnAnswersCount()

        soundManager.playSound(if (answer) Sounds.ANSWER_CORRECT else Sounds.ANSWER_WRONG)

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

    fun nativeAdLoaded(ad: UnifiedNativeAd) {
        nativeAdLiveData.value = ad
    }

    fun nativeAdShown() {
        if(nativeAdEnabled) commandCallback.value = Command.LOAD_NATIVE_AD
    }

    fun onFinishGameAccepted() {
        launch {
            withContext(dispatchers.ioDispatcher) {
                saveGameState()
                Game.portionClear()
            }
            commandCallback.value = Command.EXIT
        }
    }

    fun finishRound() {
        if (!roundFinished) {
            Game.beginNextRound()
            roundFinished = true
        }

        launch {
            if (Game.hasRound()) {
                withContext(dispatchers.ioDispatcher) {
                    saveGameState()
                }
                commandCallback.value = Command.START_NEXT_ROUND
            } else {
                withContext(dispatchers.ioDispatcher) {
                    dataProvider.deleteState(Game.state.gameId)
                }
                commandCallback.value = Command.SHOW_GAME_RESULTS
                anal.gameFinished()
            }
        }

    }

    private fun showAds() {
        if(interstitialEnabled) {
            commandCallback.value = Command.SHOW_INTERSTITIAL_ADS
        }
    }

    fun startTurn() {
        timeLeft = Game.getSettings().time
        wordLiveData.value = round.getWord()
        timerLiveData.value = timeLeft

        commandCallback.value = Command.START_TURN
    }

    private fun finishTurn() {
        round.turnFinished = true
        commandCallback.value = Command.FINISH_TURN
        ticker?.cancel()
    }

    fun nextTurn(checkedIds: List<Long>) {
        launch(dispatchers.ioDispatcher) {
            saveGameState()
        }

        round.turnFinished = false
        round.applyTurnResults(checkedIds)

        if (round.getWord() != null) {
            answeredCountLiveData.value = 0 to 0
            commandCallback.value = Command.GET_READY
            showTimeAds()
        } else {
            rounResultLiveData.value = Game.getRoundResults()
            showAds()
            commandCallback.value = Command.SHOW_ROUND_RESULTS
        }

        //Just for debug
        round.printHatContaining()
    }

    private fun showTimeAds() {
        if (System.currentTimeMillis() > adsShowedTime + interstitialDelay) {
            adsShowedTime = System.currentTimeMillis()
            showAds()
        }
    }

    fun toggleTimer() {
        if(timerStarted) {
            pauseTimer()
        } else {
            startTimer()
        }
        timerStarted = !timerStarted
        timerStateLiveData.value = timerStarted
    }

    fun onGamePaused() {
        pauseTimer()
    }

    fun onGameResumed() {
        if(timerStarted) startTimer()
    }

    private fun startTimer() {
        launch {
            ticker?.cancel()
            ticker = ticker(1000, 1000)
            ticker?.consumeEach {
                timerLiveData.value = --timeLeft
                when(timeLeft) {
                   10 -> soundManager.playSound(Sounds.TIME_WARNING)
                   1 ->  soundManager.playSound(Sounds.TIME_OVER)
                   0 -> finishTurn()
                }
            }
        }
    }

    private fun pauseTimer() {
        ticker?.cancel()
    }

    fun saveState() {
        launch(dispatchers.ioDispatcher) {
            saveGameState()
        }
    }

    fun updateCurrentScoresSheet() {
        launch {
            scoresSheetLiveData.value = emptyList()
            val results = withContext(dispatchers.ioDispatcher) {
                val currentResults = Game.getGameResults()
                val roundScores = round.results

                currentResults.forEach {
                    it.team.players.forEach { player ->
                        val scores = (roundScores[player.id] ?: 0) + (it.scoresMap[player.id] ?: 0)
                        it.scoresMap[player.id] = scores
                    }
                }

                currentResults
            }

            scoresSheetLiveData.value = results
        }
    }

    private fun saveGameState() {
        dataProvider.insertState(Game.state)
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
        LOAD_NATIVE_AD,
        EXIT
    }
}