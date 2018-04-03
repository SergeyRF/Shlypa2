package com.example.sergey.shlypa2

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by alex on 3/29/18.
 */
class HatTests {

    var hat  = Hat()

    @Before fun setup() {
        val wordsList = listOf<Word>(Word("Hello"), Word("One"), Word("Two")
                , Word("Three"), Word("Four"))

        hat.create(wordsList)
    }

    @Test fun countTest() {
        var word : Word? = hat.getWord()
        var count = 1
        while (true) {
            hat.answer(false, 0)
            word = hat.getWord()
            if(word == null) break
            else count++
        }

        assertEquals(5, count)
    }

    @Test fun answerCountTest() {
        hat.getWord()
        assertEquals(0, hat.answeredWords.size)

        for(i in 0..4) {
            println("Test case num $i")
            hat.answer(true, 0)
        }

        assertEquals(5, hat.answeredWords.size)
    }
}