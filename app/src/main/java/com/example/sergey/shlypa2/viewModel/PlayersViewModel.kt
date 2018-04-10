package com.example.sergey.shlypa2.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.Team

/**
 * Created by alex on 3/31/18.
 */
class PlayersViewModel : ViewModel(){

    private val playersLiveData = MutableLiveData<List<Player>>()
    private val teamsLiveData = MutableLiveData<List<Team>>()
    init {
        updateData()
    }

    fun getPlayersLiveData() : LiveData<List<Player>> = playersLiveData
    fun getTeamsLiveData() : LiveData<List<Team>> = teamsLiveData


    fun addPlayer(player: Player) : Boolean{
        val success = Game.addPlayer(player)
        updateData()

        return success
    }

    fun removePlayer(player: Player) {
        Game.removePlayer(player)
        updateData()
    }



    private fun updateData() {
        playersLiveData.value = Game.getPlayers()
        teamsLiveData.value = Game.getTeams()
    }
}