package com.example.sergey.shlypa2.screens.words_in

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.data.PlayersRepository
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.utils.anal.AnalSender
import com.example.sergey.shlypa2.utils.coroutines.CoroutineAndroidViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class WordsViewModel(application: Application,
                     private val dataProvider: DataProvider,
                     private val playersRepository: PlayersRepository,
                     private val dispatchers: DispatchersProvider,
                     private val anal: AnalSender)
    : CoroutineAndroidViewModel(dispatchers.uiDispatcher, application) {


    private val wordSize = Game.getSettings().word
    private val playerList = playersRepository.playersList
    private var playerPos = 0
    private val wordsList = mutableListOf<Word>()
    private val randomAllowed = Game.getSettings().allowRandomWords
    private var randomWords: ArrayDeque<Word> = ArrayDeque()

    val wordsLiveData = MutableLiveData<Pair<List<Word>, Boolean>>()
    val animateLiveData = MutableLiveData<Boolean>()
    val playerLiveData = MutableLiveData<Player>()
    val buttonNextLiveData = MutableLiveData<Pair<Boolean, Int>>()
    val inputFinishCallBackLD = MutableLiveData<Boolean>()
    val ediTextChangedLiveData = MutableLiveData<Boolean>()


    init {
        allWordsRandom()
    }

    private fun allWordsRandom() {
        launch {
            if (Game.getSettings().all_word_random) {
                val wordNeeds = playersRepository.getPlayersSize() * Game.getSettings().word
                withContext(dispatchers.ioDispatcher) {
                    val dbWords = dataProvider.getRandomWords(wordNeeds, Game.getSettings().typeId)
                    Game.addWord(dbWords)
                }
                applyGameAndStart()
            } else {
                wordsLiveData.value = Pair(wordsList, randomAllowed)
                playerLiveData.value = playerList[playerPos]
                checkNextButton()
            }
        }
    }

    fun addWord(word: String): Int {
        wordsList.add(Word(word))
        notifyWords()
        anal.wordAdded(false)
        return wordSize - wordsList.size
    }

    fun deleteWord(wordPosition: Int) {
        wordsList.removeAt(wordPosition)
        notifyWords()
    }

    fun changeWord(wordPosition: Int) {
        launch(dispatchers.ioDispatcher) {
            if (randomWords.size < 1) loadRandomWords()
            withContext(dispatchers.uiDispatcher) {
                wordsList[wordPosition] = randomWords.poll()
                wordsLiveData.value = Pair(wordsList, randomAllowed)
            }
        }
    }

    private fun notifyWords() {
        wordsLiveData.value = Pair(wordsList, randomAllowed)
        checkAnimate()
        checkEditText()
        checkNextButton()
    }

    private fun checkEditText() {
        ediTextChangedLiveData.value = wordsList.size < wordSize
    }


    private fun checkNextButton() {
        if (wordsList.size == wordSize) {
            buttonNextLiveData.value = Pair(true, R.string.play)
        } else {
            buttonNextLiveData.value = Pair(randomAllowed, R.string.add_random)
        }
    }

    private fun checkAnimate() {
        animateLiveData.value = (wordsList.size > 0)
    }

    fun clickNext() {
        if (randomAllowed && wordsList.size < wordSize) {
            fillWithRandomWords()
        } else {
            nextPlayer()
        }
    }

    private fun fillWithRandomWords() {
        val needWord = wordSize - wordsList.size
        launch(dispatchers.ioDispatcher) {
            if (randomWords.size < needWord) loadRandomWords()
            repeat(needWord) {
                wordsList.add(randomWords.poll())
            }
            anal.wordAdded(true)
            withContext(dispatchers.uiDispatcher) { notifyWords() }
        }
    }

    private fun nextPlayer() {
        playerPos++
        Game.addWord(wordsList)
        wordsList.clear()
        playerList.getOrNull(playerPos)?.let {
            playerLiveData.value = it
            notifyWords()
        } ?: run {
            applyGameAndStart()
        }
    }

    private fun applyGameAndStart() {
        Game.setPlayers(playersRepository.playersList)
        Game.setTeams(playersRepository.getTeams())
        playerPos = 0
        inputFinishCallBackLD.value = true
    }

    private fun loadRandomWords() {
        val dbWords = dataProvider.getRandomWords(100, Game.getSettings().typeId)
        val unicWords: List<Word> = dbWords.filter { !Game.getWords().contains(it) && !wordsList.contains(it) }
        Timber.d("unic words size ${unicWords.size}")

        randomWords.addAll(unicWords)
    }
}