package com.example.sergey.shlypa2

import java.util.*
/**
 * Created by alex on 4/3/18.
 */
class Round(val words: List<Word>) {

    var wordsQueue : ArrayDeque<Word>
    private var currentWord: Word? = null
    var answeredWords = mutableListOf<Word>()

    private var wordsAnsweredByPlayer = mutableListOf<Word>()

    private var currentTeam : Team = Game.getCurrentTeam()

    private var currentPlayer : Player = currentTeam.getPlayer()

    init {
        val listCopy = words.toMutableList()
        Collections.shuffle(listCopy)

        wordsQueue = ArrayDeque<Word>()
        wordsQueue.addAll(listCopy)
    }

    fun getPlayer() : Player {
        return currentPlayer
    }

    fun nextPlayer() : Player {
        wordsAnsweredByPlayer.clear()

        currentTeam = Game.nextTeam()

        currentPlayer = currentTeam.nextPlayer()

        return currentPlayer
    }

    fun getWord() : Word? {
        if(currentWord == null && !wordsQueue.isEmpty()) {
            currentWord = wordsQueue.poll()
        }

        return currentWord
    }

    fun answer(correct : Boolean) {
        if(currentWord != null) {
            currentWord!!.right = correct
            currentWord!!.answeredBy = currentPlayer.id
            answeredWords.add(currentWord!!)
            wordsAnsweredByPlayer.add(currentWord!!)
        }

        currentWord = null
        getWord()
    }

    /**
     * Return word into hat
     */
    fun returnWord(word: Word) {
        wordsQueue.add(word)
        answeredWords.remove(word)
        wordsAnsweredByPlayer.remove(word)
    }

    fun countScores() {
        Game.getTeams().flatMap { it.players }
                .forEach{player -> answeredWords.forEach{if (it.answeredBy == player.id) player.scores++}}
    }

}

