package com.example.sergey.shlypa2

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by alex on 3/31/18.
 */
class GameTests {

    @Before
    fun setup() {
        Game.clear()
        Game.addPlayer(Player("John"))
        Game.addPlayer(Player("Jack"))
        Game.addPlayer(Player("Ashot"))
        Game.addPlayer(Player("Vazgen"))
        Game.addPlayer(Player("Rafic"))
        Game.addPlayer(Player("Gurgen"))
        Game.addPlayer(Player("Jumshoot"))
        Game.addPlayer(Player("Magamet"))
    }

    @Test fun createTeamsTest() {
        Game.createTeams(2)
        assertEquals(2, Game.getTeams().size)

        for(team in Game.getTeams()) {
            println(team)
        }
    }

    @Test fun createThreeTeamsTest() {
        Game.createTeams(3)
        assertEquals(3, Game.getTeams()[0].players.size)
        assertEquals(2, Game.getTeams()[2].players.size)

        for(team in Game.getTeams()) {
            println(team)
        }
    }
}