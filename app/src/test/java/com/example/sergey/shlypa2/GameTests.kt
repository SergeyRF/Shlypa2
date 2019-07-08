package com.example.sergey.shlypa2

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.data.PlayersRepository
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.TeamWithScores

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

    @Test fun countGameResultsTest() {
        val teamsList = TestDataProvider.getTestTeams(1, 4)
        val scoredTeamList = teamsList.map { TeamWithScores(it) }

        val resultsList : MutableList<Map<Long, Int>> = mutableListOf()

        for(i in 0 until 3) {
            val map : Map<Long, Int> = mutableMapOf(1L to 3, 3L to 2)
            resultsList.add(map)
        }

        val countedScores = Game.calculateGameResults(scoredTeamList, resultsList)
        println(countedScores[0].scoresMap)

        assertEquals(scoredTeamList[0].scoresMap[1], 9)
        assertEquals(scoredTeamList[0].scoresMap[3], 6)
    }
}