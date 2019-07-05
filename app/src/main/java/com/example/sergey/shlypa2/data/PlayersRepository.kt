package com.example.sergey.shlypa2.data

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.db.DataProvider
import java.util.*

class PlayersRepository(
        private val dataProvider: DataProvider
) {
    private val players = mutableMapOf<Long, Player>()
    private val teams = mutableListOf<Team>()

    val maxTeamsCount: Int
        get() = players.size / 2

    fun addPlayer(player: Player): Boolean {
        val playerExists = players.values
                .any {
                    it.id == player.id || it.name.equals(player.name, ignoreCase = true)
                }

        return if (playerExists) {
            false
        } else {
            players[player.id] = player
            true
        }
    }

    fun removePlayer(player: Player) {
        players.remove(player.id)
    }

    fun reNamePlayer(player: Player) {
        players[player.id]?.name = player.name
    }

    fun getPlayers(): List<Player> {
        return players.values.toList()
    }

    fun getTeams() = teams as List<Team>

    fun createTeams(count: Int) {
        val shuffledPlayers: MutableList<Player> = players.values.toMutableList()
        shuffledPlayers.shuffle()

        val playersQueue = ArrayDeque<Player>()
        playersQueue.addAll(shuffledPlayers)

        teams.clear()

        val shuffledTeams = dataProvider.teamNames.toMutableList().shuffled()


        for (i in 0 until count) {
            val teamName = shuffledTeams.getOrElse(i) { "Team $i" }
            teams.add(Team(teamName))
        }

        var currentTeam = 0
        //todo refactor this shit  !!!
        while (playersQueue.isNotEmpty()) {
            teams[currentTeam].players.add(playersQueue.remove())

            currentTeam = if (currentTeam >= teams.size - 1) 0 else currentTeam + 1
        }
    }
}