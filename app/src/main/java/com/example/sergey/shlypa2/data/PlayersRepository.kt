package com.example.sergey.shlypa2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.PlayerType
import java.util.*

class PlayersRepository(
        private val dataProvider: DataProvider
) {
    private val players = Collections.synchronizedMap(mutableMapOf<Long, Player>())
    private var teams = Collections.synchronizedList(mutableListOf<Team>())
    private val playersLiveData = MutableLiveData<List<Player>>()
    private val teamsLiveData = MutableLiveData<List<Team>>()

    private val randomPlayers: Queue<Player> by lazy {
        LinkedList(dataProvider.getPlayers().shuffled())
    }

    private val maxTeamsCount: Int
        get() = players.size / 2

    val playersList: List<Player>
        get() = players.values.toList()

    fun getTeamsLiveData(): LiveData<List<Team>> = Transformations.map(teamsLiveData) { it }

    fun getPlayersLiveData(): LiveData<List<Player>> = Transformations.map(playersLiveData) { it }

    /**
     * Returns a list with players saved by user and
     * not added to the game yet
     */
    fun getUserSavedPlayers(): List<Player> {
        return dataProvider.getPlayersUser()
                .filterNot { players.containsKey(it.id) }
                .toList()
    }

    /**
     * Is there any player in the data base which is
     * created by a user and not added to the game yet
     */
    fun hasGoodUserPlayers(): Boolean {
        return dataProvider.getPlayersUser()
                .any { !players.containsKey(it.id) }
    }

    fun getPlayersSize(): Int = players.size

    fun getTeams() = teams.toList()

    fun removePlayer(player: Player) {
        players.remove(player.id)
        // return a player to the random queue
        if (player.type == PlayerType.STANDARD) {
            randomPlayers.offer(player)
        }
        notifyPlayers()
    }

    fun reNamePlayer(player: Player) {
        if (player.type == PlayerType.USER) {
            players[player.id]?.name = player.name
        } else {
            players.remove(player.id)
            //set id to zero before inserting
            player.type = PlayerType.USER
            player.id = 0
            player.id = dataProvider.insertPlayer(player)
            players[player.id] = player
        }
        notifyPlayers()
    }

    fun addPlayer(player: Player) {
        players[player.id] = player
        notifyPlayers()
    }

    fun addNewPlayer(name: String, avatar: String): Boolean {
        val playerExists = players.values
                .any {
                    it.name.equals(name, ignoreCase = true)
                }
        if (playerExists) return false

        val newPlayer = Player(name, avatar = avatar, type = PlayerType.USER)
        newPlayer.id = dataProvider.insertPlayer(newPlayer)

        players[newPlayer.id] = newPlayer
        notifyPlayers()
        return true
    }

    fun addRandomPlayer() {
        val newPlayer = randomPlayers.poll() ?: return
        players[newPlayer.id] = newPlayer
        notifyPlayers()
    }

    fun renameTeam(newName: String, oldName: String) {
        teams.find { it.name == oldName }
                ?.name = newName
        notifyTeams()
    }

    fun incrementTeams(): Boolean {
        val newTeamsCount = teams.size + 1
        return if (newTeamsCount <= maxTeamsCount) {
            createTeams(newTeamsCount)
            true
        } else false
    }

    fun reduceTeams(): Boolean {
        val newTeamsCount = teams.size - 1
        return if (newTeamsCount >= 2) {
            createTeams(newTeamsCount)
            true
        } else false
    }

    fun shuffleTeams() {
        createTeams(teams.size)
    }

    /**
     * Creates 2 teams if teams haven't been created yet or
     * add players which currently not in any team into existing teams
     */
    fun initTeams() {
        if (teams.isEmpty()) {
            createTeams(2)
            return
        }

        val teamPlayers = teams.map { it.players }.flatten()
        playersList.filterNot { teamPlayers.contains(it) }
                .forEachIndexed { index, player ->
                    teams[index % teams.size].players.add(player)
                }

        notifyTeams()
    }

    private fun createTeams(count: Int) {
        val shuffledNames = dataProvider.teamNames.toList().shuffled()
        val newTeams = (0 until count)
                .map { Team(shuffledNames.getOrElse(it) {"Team $it"}) }

        players.values
                .shuffled()
                .forEachIndexed { index, player ->
            newTeams[index % newTeams.size].players.add(player)
        }

        teams.clear()
        teams.addAll(newTeams)
        notifyTeams()
    }

    fun changeTeams(teams: List<Team>) {
        this.teams = teams.toMutableList()
    }

    fun clear() {
        players.clear()
        teams.clear()
        notifyPlayers()
        notifyTeams()
    }

    private fun notifyPlayers() {
        playersLiveData.postValue(players.values.toList())
    }

    private fun notifyTeams() {
        teamsLiveData.postValue(teams)
    }
}