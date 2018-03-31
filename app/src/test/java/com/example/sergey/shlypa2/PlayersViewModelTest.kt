package com.example.sergey.shlypa2

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PlayersViewModelTest {

    lateinit var playersViewModel: PlayersViewModel

    @Before
    fun setup() {
        Game.clear()
        playersViewModel = PlayersViewModel()
        playersViewModel.addPlayer(Player("John"))
        playersViewModel.addPlayer(Player("Jack"))
        playersViewModel.addPlayer(Player("Soul"))
        println("Before $playersViewModel")
    }

    @Test
    fun testPlayersInsertCount() {
        val playersLiveData = playersViewModel.getPlayersLiveData()
        assertEquals(3, playersLiveData.value!!.size)

        playersViewModel.addPlayer(Player("Matt"))
        assertEquals(4, playersLiveData.value!!.size)
    }

    @Test
    fun testPlayersWithSameNameCantBeInserted() {
        playersViewModel.addPlayer(Player("John"))

        val playersLiveDate = playersViewModel.getPlayersLiveData()
        assertEquals(3, playersLiveDate.value!!.size)
    }

    @Test
    fun testPlayerDeletionWorksCorrect() {
        playersViewModel.removePlayer(Player("John"))

        val playersLiveData = playersViewModel.getPlayersLiveData()
        assertEquals(2, playersLiveData.value!!.size)
    }
}