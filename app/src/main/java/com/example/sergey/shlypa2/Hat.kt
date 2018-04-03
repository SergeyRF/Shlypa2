package com.example.sergey.shlypa2

import java.util.*

/**
 * Created by alex on 3/29/18.
 */
class Hat {
    var wordsQueue = ArrayDeque<Word>()
    var answeredWords = mutableListOf<Word>()

    var currentWord: Word? = null

    fun create(words : List<Word>) {
        val listCopy = words.toMutableList()
        Collections.shuffle(listCopy)
        wordsQueue.addAll(listCopy)
    }

    fun getWord() : Word? {
       if(currentWord == null && !wordsQueue.isEmpty()) {
            currentWord = wordsQueue.poll()
       }

       return currentWord
    }

    fun answer(correct : Boolean, playerId : Int) {
        if(currentWord != null) {
            currentWord?.right = correct
            currentWord?.answeredBy = playerId
            answeredWords.add(currentWord!!)
        }

        currentWord = null
        getWord()
    }

    fun returnWord(word: Word) = wordsQueue.add(word)
}