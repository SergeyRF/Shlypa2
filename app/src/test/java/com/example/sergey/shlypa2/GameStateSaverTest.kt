package com.example.sergey.shlypa2

import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.utils.GameStateSaver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertEquals
@RunWith(RobolectricTestRunner::class)
class GameStateSaverTest {

    lateinit var gameState: GameState
    val saver = GameStateSaver(RuntimeEnvironment.application)

    @Before
    fun init() {
        gameState = GameState()
    }
}