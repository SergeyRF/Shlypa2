package com.example.sergey.shlypa2

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testStrategy() {

        val someAnimal = Animal()
        someAnimal.makeSound()
        someAnimal.makeSound = fun() {
            println("Woof")
        }
        someAnimal.makeSound

    }

}

class Animal {
    var makeSound = fun() {
        println("Mew")
    }
}