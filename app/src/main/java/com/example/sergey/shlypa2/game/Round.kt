package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word
import timber.log.Timber
import java.util.*

/**
 * Created by alex on 4/3/18.
 */
class Round(
        words: List<Word>,
        var wordsQueue: ArrayDeque<Word> = ArrayDeque(),
        var wordsAnsweredByPlayer: MutableList<Word> = mutableListOf(),
        var results: MutableMap<Long, Int> = mutableMapOf(),
        var currentTeam: Team = Game.getCurrentTeam(),
        var currentPlayer: Player = currentTeam.getPlayer(),
        var descriptor: RoundDescriptors = RoundDescriptors.WORD_BY_SENTENCES,
        private var currentWord: Word? = null,
        var turnFinished: Boolean = false) {

    init {
        val listCopy = words.toMutableList()
        Collections.shuffle(listCopy)
        wordsQueue.addAll(listCopy)
    }

    fun getPlayer(): Player {
        return currentPlayer
    }

    fun applyTurnResults(checkedIds: List<Long>) {
        var scores = 0
        results[currentPlayer.id]?.let {
            scores = it
        }

        wordsAnsweredByPlayer.forEach {
            it.right = checkedIds.contains(it.id)
            if (it.right) {
                scores++
            } else {
                if (Game.getSettings().penaltyInclude) {
                    scores -= Game.getSettings().penaltyPoint
                }

                if (Game.getSettings().returnSkipedToHat) {
                    wordsQueue.add(it)
                }
            }
        }

        results[currentPlayer.id] = scores
        wordsAnsweredByPlayer.clear()

        currentTeam = Game.nextTeam()
        currentPlayer = currentTeam.nextPlayer()
    }

    fun getWord(): Word? {
        if (currentWord != null) {
            wordsQueue.add(currentWord)
        }
        if (!wordsQueue.isEmpty()) {
            val shuffleList = wordsQueue.toMutableList()
            shuffleList.shuffle()
            wordsQueue.clear()
            wordsQueue.addAll(shuffleList)
            currentWord = null
        }
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
    fun getTurnAnswersCount(): Pair<Int, Int> {
        var correct = 0
        var skipped = 0

        wordsAnsweredByPlayer.forEach {
            if (it.right) correct++
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

