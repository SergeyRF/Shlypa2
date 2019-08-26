package com.example.sergey.shlypa2

import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.utils.GameStateSaver
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GameStateSaverTest {

    lateinit var gameState: GameState
    val saver = GameStateSaver()

    @Before
    fun init() {
        gameState = GameState()
    }
}