package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.extensions.random
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.PlayerType
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by alex on 3/31/18.
 */
class PlayersViewModel(application: Application) : AndroidViewModel(application) {

    private val playersLiveData = MutableLiveData<List<Player>>()
    private val teamsLiveData = MutableLiveData<List<Team>>()
    private val avatarLiveData = MutableLiveData<String>()
    val toastResLD = MutableLiveData<Int>()

    private val dataProvider = DataProvider(application)

    val commandLiveData = SingleLiveEvent<Command>()
    val titleLiveData = MutableLiveData<Int>()

    val listOfAvatars: MutableList<String> = mutableListOf()

    init {
        updateData()
    }

    fun getPlayersLiveData(): LiveData<List<Player>> = playersLiveData

    fun getTeamsLiveData(): LiveData<List<Team>> = teamsLiveData

    fun getAvatarLiveData(): LiveData<String> {
        if (listOfAvatars.isEmpty()) {
            doAsync {
                synchronized(this) { loadAvatars() }
            }
        }
        return avatarLiveData
    }

    //todo do not add players with the same name
    fun addPlayer(player: Player): LiveData<Boolean> {
        val singleLiveEvent = SingleLiveEvent<Boolean>()

        doAsync {
            player.id = dataProvider.insertPlayer(player)
            val success = Game.addPlayer(player)

            if (success) {
                uiThread {
                    avatarLiveData.value = listOfAvatars.random()
                    updateData()
                    singleLiveEvent.value = true
                }
            }
        }

        return singleLiveEvent
    }

    fun removePlayer(player: Player) {
        Game.removePlayer(player)
        updateData()
    }

    fun reNamePlayer(player: Player) {
        doAsync {
            if (player.type == PlayerType.USER) {
                Game.reNamePlayer(player)
            } else {
                Game.removePlayer(player)
                //set id to zero before inserting
                player.type = PlayerType.USER
                player.id = 0
                player.id = dataProvider.insertPlayer(player)
                Game.addPlayer(player)
            }
        }
    }

    fun addRandomPlayer() {
        doAsync {
            val playersList = dataProvider.getPlayers()

            val player: Player? = playersList.find { !Game.getPlayers().contains(it) }
            if (player != null) Game.addPlayer(player)

            uiThread {
                updateData()
            }
        }
    }

    fun addPlayer(name: String) {
        if (name.isBlank()) {
            toastResLD.value = R.string.player_name_empty
            return
        }

        doAsync {
            val player = Player(name, avatar = avatarLiveData.value ?: "")
            player.id = dataProvider.insertPlayer(player)
            val success = Game.addPlayer(player)

            uiThread {
                if (success) {
                    avatarLiveData.value = listOfAvatars.random()
                    updateData()
                } else {
                    toastResLD.value = R.string.name_not_unic
                }
            }
        }
    }

    private fun loadAvatars() {
        listOfAvatars.addAll(dataProvider.getListOfAvatars())
        avatarLiveData.postValue(listOfAvatars.random())
    }

    fun initTeams() {
        Game.createTeams(2)
        updateData()
    }

    fun addTeam() {
        val teamsCount = Game.getTeams().size + 1
        if (teamsCount <= Game.maxTeamsCount()) {
            Game.createTeams(teamsCount)
            updateData()
        } else {
            Toast.makeText(getApplication(), R.string.cant_create_teams, Toast.LENGTH_SHORT).show()
        }
    }

    fun reduceTeam() {
        if (Game.getTeams().size > 2) {
            val teamsCount = Game.getTeams().size - 1
            Game.createTeams(teamsCount)
            updateData()
        } else {
            Toast.makeText(getApplication(), R.string.two_team_min, Toast.LENGTH_SHORT).show()
        }
    }

    fun shuffleTeams() {
        Game.createTeams(Game.getTeams().size)
        updateData()
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