package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word
import java.util.*

/**
 * Created by alex on 4/3/18.
 */
class Round(val words: List<Word>) {

    var description = "Description"
    var rules = "Guess word or fuck yourself"

    var wordsQueue: ArrayDeque<Word>
    var wordsAnsweredByPlayer = mutableListOf<Word>()

    var results: MutableMap<Long, MutableList<Long>> = mutableMapOf()

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
        val playerList: MutableList<Long>

        if (results.containsKey(currentPlayer.id)) {
            playerList = results[currentPlayer.id]!!
        } else {
            playerList = mutableListOf()
            results[currentPlayer.id] = playerList
        }

        wordsAnsweredByPlayer.forEach {
            if (it.right) playerList.add(it.id)
        }

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

    /**
     * Return word into hat
     */
    fun returnWord(word: Word) {
        wordsQueue.add(word)
        wordsAnsweredByPlayer.remove(word)
    }

    fun getResults(): RoundResults {
        return RoundResults(results)
    }

}

