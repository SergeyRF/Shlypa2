package com.example.sergey.shlypa2.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.data.PlayersRepository
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.WordType
import com.example.sergey.shlypa2.utils.anal.AnalSender
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.core.context.GlobalContext.getOrNull
import timber.log.Timber
import java.util.*

/**
 * Created by sergey on 4/3/18.
 */
class WordsViewModel(
        application: Application,
        val dataProvider: DataProvider,
        val playersRepository: PlayersRepository,
        val anal: AnalSender) : AndroidViewModel(application) {

    private val wordsLiveData = MutableLiveData<List<Word>>()
    private val playerLivaData = MutableLiveData<Player>()

    val inputFinishCallBack = MutableLiveData<Boolean>()

    private val needWord = MutableLiveData<Boolean>()

    private var pos: Int = 0
    private var words: MutableList<Word> = mutableListOf()

    private var randomWords: ArrayDeque<Word> = ArrayDeque()

    init {
        updateData()
    }

    fun nextPlayer() {
        for (word in words) {
            Game.addWord(word)
        }

        words.clear()
        pos++

        if (pos < playersRepository.getPlayers().size) {
            updateData()
        } else {
            applyGameAndStart()
        }
    }

    fun getNeedWordsLive(): LiveData<Boolean> {
        needWord.value = needWord()
        return needWord
    }

    fun needWord(): Boolean {
        Timber.d("words size ${words.size} game settings words ${Game.getSettings().word}")
        return words.size < Game.getSettings().word
    }


    fun needWordSize(): Int = Game.getSettings().word - words.size


    fun randomAllowed(): Boolean = Game.getSettings().allowRandomWords

    fun getPlayerLiveData(): LiveData<Player> {
        return playerLivaData
    }

    fun getWordsLiveData(): LiveData<List<Word>> = wordsLiveData

    fun addWord(s: String) {
        words.add(Word(s))
        updateData()
        anal.wordAdded(false)
    }

    fun reNameWord(word: Word) {
        //word.type = WordType.USER
        //fixme
    }

    fun fillWithRandomWords() {

        doAsync {
            val needWordsCount = Game.getSettings().word - words.size
            if (randomWords.size < needWordsCount) loadRandomWords()

            Timber.d("queue size after loading = ${randomWords.size}")
            for (i in 0 until needWordsCount)
                words.add(randomWords.poll())

            uiThread { updateData() }
        }

    }

    private fun applyGameAndStart() {
        Game.setPlayers(playersRepository.getPlayers())
        Game.setTeams(playersRepository.getTeams())
        pos = 0
        inputFinishCallBack.value = true
    }


    private fun loadRandomWords() {
        val dbWords = dataProvider.getRandomWords(100, Game.getSettings().typeId)
        for (w in dbWords) Timber.d("$w")

        val unicWords: List<Word> = dbWords.filter { !Game.getWords().contains(it) && !words.contains(it) }
        Timber.d("unic words size ${unicWords.size}")

        randomWords.addAll(unicWords)
        anal.wordAdded(true)
    }

    fun deleteWord(word: Word) {
        words.remove(word)
        updateData()
    }

    fun newRandomWord(word: Word) {
        if (randomWords.isEmpty()) {
            doAsync {
                loadRandomWords()
                words[words.indexOf(word)] = randomWords.poll()
                uiThread {
                    updateData()
                }
            }
        } else {
            words[words.indexOf(word)] = randomWords.poll()
            updateData()
        }
    }

    private fun updateData() {
        wordsLiveData.value = words
        playerLivaData.value = playersRepository.getPlayers().getOrNull(pos)
        needWord.value = needWord()
    }
}