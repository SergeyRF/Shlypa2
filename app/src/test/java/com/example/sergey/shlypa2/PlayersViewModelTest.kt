/*
package com.example.sergey.shlypa2

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.screens.players.PlayersViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProviderImpl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PlayersViewModelTest {

    lateinit var playersViewModel: PlayersViewModel

    @Before
    fun setup() {
        Game.clear()
        playersViewModel = PlayersViewModel(RuntimeEnvironment.application, DispatchersProviderImpl())
        playersViewModel.addNewPlayer("John")
        playersViewModel.addNewPlayer("Jack")
        playersViewModel.addNewPlayer("Soul")
        println("Before $playersViewModel")
    }

    @Test
    fun testPlayersInsertCount() {
        val playersLiveData = playersViewModel.getPlayersLiveData()
        assertEquals(3, playersLiveData.value!!.size)

        playersViewModel.addNewPlayer("Matt")
        assertEquals(4, playersLiveData.value!!.size)
    }

    @Test
    fun testPlayersWithSameNameCantBeInserted() {
        playersViewModel.addNewPlayer("John")

        val playersLiveDate = playersViewModel.getPlayersLiveData()
        assertEquals(3, playersLiveDate.value!!.size)
    }

    @Test
    fun testPlayerDeletionWorksCorrect() {
        playersViewModel.removePlayer(Player("John"))

        val playersLiveData = playersViewModel.getPlayersLiveData()
        assertEquals(2, playersLiveData.value!!.size)
    }
}*/
