package com.example.sergey.shlypa2

import com.example.sergey.shlypa2.beans.GameState
import com.example.sergey.shlypa2.utils.GameStateSaver
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class GameStateSaverTest {

    lateinit var gameState: GameState
    val saver = GameStateSaver(RuntimeEnvironment.application)

    @Before
    fun init() {
        gameState = GameState()
    }
}