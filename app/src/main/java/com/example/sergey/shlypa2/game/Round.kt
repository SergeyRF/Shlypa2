package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word
import timber.log.Timber
import java.util.*

/**
 * Created by alex on 4/3/18.
 */
class Round(val words: List<Word>) {

    var description = 1
    var rules = 2
    var name = 3

    var wordsQueue: ArrayDeque<Word>
    var wordsAnsweredByPlayer = mutableListOf<Word>()

    var results: MutableMap<Long, Int> = mutableMapOf()

    var currentTeam: Team = Game.getCurrentTeam()
    var currentPlayer: Player = currentTeam.getPlayer()

    private var currentWord: Word? = null


    init {
        val listCopy = words.toMutableList()
        Collections.shuffle(listCopy)

        wordsQueue = ArrayDeque()
        wordsQueue.addAll(listCopy)
    }

    fun getPlayer(): Player {
        return currentPlayer
    }

    fun nextPlayer(): Player {
        var playerScores = 0

        if (results.containsKey(currentPlayer.id)) {
            playerScores = results[currentPlayer.id]!!
        }

        wordsAnsweredByPlayer.forEach {
            if (it.right) {
                playerScores++
            } else {
                if(Game.getSettings().minusBal) {
                    playerScores -= Game.getSettings().numberMinusBal
                }

                if(Game.getSettings().returnSkipedToHat) {
                    wordsQueue.add(it)
                }
            }
        }

        results[currentPlayer.id] = playerScores

        wordsAnsweredByPlayer.clear()

        currentTeam = Game.nextTeam()
        currentPlayer = currentTeam.nextPlayer()

        return currentPlayer
    }

    fun getWord(): Word? {
        if (currentWord == null && !wordsQueue.isEmpty()) {
            currentWord = wordsQueue.poll()
        }

        return currentWord
    }

    fun answer(correct: Boolean) {
        currentWord?.let {
            it.right = correct
            it.answeredBy = currentPlayer.id
            wordsAnsweredByPlayer.add(it)
        }

        currentWord = null
        getWord()
    }

    /**
     * Returns count of words answered by current player
     * first value - correct, second  - skipped
     */
    fun getTurnAnswersCount() : Pair<Int, Int> {
        var correct = 0
        var skipped = 0

        wordsAnsweredByPlayer.forEach {
            if(it.right) correct++
            else skipped++
        }

        return Pair(correct, skipped)
    }

    fun getResults(): RoundResults {
        return RoundResults(results)
    }

    fun printHatContaining() {
        val hatList = wordsQueue.toList()
        hatList.forEach {
            Timber.d("Word in hat ${it.word}")
        }
    }
}

