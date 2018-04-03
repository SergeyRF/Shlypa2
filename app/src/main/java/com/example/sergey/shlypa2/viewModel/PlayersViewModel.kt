package com.example.sergey.shlypa2.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.Player
import com.example.sergey.shlypa2.game.Team

/**
 * Created by alex on 3/31/18.
 */
class PlayersViewModel : ViewModel(){

    private val playersLiveData = MutableLiveData<List<Player>>()
    private val teamsLiveData = MutableLiveData<List<Team>>()

    fun getPlayersLiveData() : LiveData<List<Player>> = playersLiveData
    fun getTeamsLiveData() : LiveData<List<Team>> {
        if (teamsLiveData.value==null){
            createTeams()
        }
        return teamsLiveData
    }


    fun addPlayer(player: Player) : Boolean{
        val success = Game.addPlayer(player)
        updateData()

        return success
    }

    fun removePlayer(player: Player) {
        Game.removePlayer(player)
        updateData()
    }

    fun createTeams(){
        Game.createTeams(Game.teamsCount)
        updateData()
    }

    fun createTeams(count : Int) : Boolean{
        return if(Game.getPlayers().size >= count) {
            Game.createTeams(count)
            updateData()
            true
        } else false
    }

    private fun updateData() {
        playersLiveData.value = Game.getPlayers()
        teamsLiveData.value = Game.getTeams()
    }
}