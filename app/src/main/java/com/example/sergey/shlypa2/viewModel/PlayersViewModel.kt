package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.widget.Toast
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Contract
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.random
import timber.log.Timber

/**
 * Created by alex on 3/31/18.
 */
class PlayersViewModel(application: Application) : AndroidViewModel(application) {

    private val playersLiveData = MutableLiveData<List<Player>>()
    private val teamsLiveData = MutableLiveData<List<Team>>()
    private val dataProvider = DataProvider(application)

    val commandLiveData = SingleLiveEvent<Command>()
    val titleLiveData = MutableLiveData<Int>()

    init {
        updateData()
    }

    fun getPlayersLiveData(): LiveData<List<Player>> = playersLiveData
    fun getTeamsLiveData(): LiveData<List<Team>> = teamsLiveData


    fun addPlayer(player: Player): Boolean {
        player.id = dataProvider.insertPlayer(player)
        val success = Game.addPlayer(player)
        updateData()

        return success
    }

    fun removePlayer(player: Player) {
        Game.removePlayer(player)
        updateData()
    }

    fun reNamePlayer(player: Player) {
        if (player.type == Contract.PlayerType.USER) {
            Game.reNamePlayer(player)
        } else {
            Game.removePlayer(player)
            //set id to zero before inserting
            player.type = Contract.PlayerType.USER
            player.id = 0
            player.id = dataProvider.insertPlayer(player)
            Game.addPlayer(player)
        }
        Timber.d("Players rename name ${player.name} id ${player.id} players size ${Game.getPlayers().size}")
//        updateData()
    }

    fun addRandomPlayer() {
        //Todo replace this ugly code
        val playersList = dataProvider.getPlayers()

        for (player in playersList) {
            Timber.d("Player from db ${player.name}")
        }

        val player: Player? = playersList.find { !Game.getPlayers().contains(it) }
        if (player != null) Game.addPlayer(player)

        updateData()
    }

    fun getRandomAvatar() : String{

        val filesList = dataProvider.getListOfAvatars()
        val randomFile = filesList.random()
        if(randomFile != null) {
            return Functions.imageNameToUrl(randomFile)
        } else return ""
    }

    fun initTeams() {
        Game.createTeams(2)
        updateData()
    }

    fun addTeam() {
        val teamsCount = Game.getTeams().size + 1
        if(teamsCount <= Game.maxTeamsCount()) {
            Game.createTeams(teamsCount)
            updateData()
        } else {
            Toast.makeText(getApplication(), R.string.cant_create_teams, Toast.LENGTH_SHORT).show()
        }
    }

    fun reduceTeam() {
        if(Game.getTeams().size > 2) {
            val teamsCount = Game.getTeams().size - 1
            Game.createTeams(teamsCount)
            updateData()
        } else {
            Toast.makeText(getApplication(), R.string.two_team_min, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData() {
        playersLiveData.value = Game.getPlayers()
        teamsLiveData.value = Game.getTeams()
    }

    fun setTitleId(resourceId: Int) {
        titleLiveData.value = resourceId
    }

    fun startTeams() {
        commandLiveData.value = Command.START_TEAMS
    }

    fun startSettings() {
        commandLiveData.value = Command.START_SETTINGS
    }

    enum class Command {
        START_TEAMS,
        START_SETTINGS
    }
}