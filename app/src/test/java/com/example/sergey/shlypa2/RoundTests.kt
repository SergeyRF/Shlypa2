package com.example.sergey.shlypa2

import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.Player
import com.example.sergey.shlypa2.game.Round
import com.example.sergey.shlypa2.game.Word
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Created by alex on 4/3/18.
 */

class RoundTests {

    lateinit var round: Round
    var playersCount = 14
    var teamsCount = 3

    var wordsCount = 50

    @Before
    fun setup() {
        Game.clear()


        for(i in 0 until playersCount) Game.addPlayer(Player("id $i"))
        Game.createTeams(teamsCount)

        for(i in 0 until wordsCount) Game.addWord(Word("Word $i"))

        round = Round(Game.getWords())
    }

    @Test
    fun testCorrectPlayersCount() {
        var count = 1

        val player = round.getPlayer()
        assertNotNull(round.getPlayer())

        while (true) {
            val nextPlayer = round.nextPlayer()
            if (nextPlayer.name == player.name) break
            else count++
        }

        val maxPlayersInTeam = Game.getTeams().maxBy { it.players.size }?.players?.size ?: 0

        assertEquals(maxPlayersInTeam * teamsCount, count)
    }

    @Test
    fun testCorrectWordsCount() {
        var count = 0
        round.getWord()
        while (true) {
            count++
            round.answer(true)
            val word = round.getWord()
            if (word == null) break
        }

        assertEquals(wordsCount, count)
    }

    @Test
    fun testScoresCount() {
        for (i in 0..4) {
            for (a in 0..9) {
                round.getWord()
                round.answer(true)
            }
            round.nextPlayer()
        }

        round.countScores()
    }

    @Test
    fun testPlayersQueue() {
        for(i in 0..50) println("${round.nextPlayer()}")
    }
}