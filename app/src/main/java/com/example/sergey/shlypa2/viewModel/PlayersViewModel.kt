package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.beans.Contract
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Dificult
import com.example.sergey.shlypa2.game.Game

/**
 * Created by alex on 3/31/18.
 */
class PlayersViewModel(application: Application) : AndroidViewModel(application) {

    private val playersLiveData = MutableLiveData<List<Player>>()
    private val teamsLiveData = MutableLiveData<List<Team>>()
    private var db = DataProvider(application)
    private val dataProvider = DataProvider(application)

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

    fun reNamePlayer(player: Player){
        if (player.type ==Contract.PlayerType.USER) {
            Game.reNamePlayer(player)
        }
        else{
            removePlayer(player)
            addPlayer(player)
        }
        updateData()
    }

    fun addRandomPlayer() {
        //Todo replace this ugly code
        val playersList = dataProvider.getPlayers()

        val player: Player? = playersList.find { !Game.getPlayers().contains(it) }
        if (player != null) Game.addPlayer(player)

        updateData()
    }


    private fun updateData() {
        playersLiveData.value = Game.getPlayers()
        teamsLiveData.value = Game.getTeams()
    }
}