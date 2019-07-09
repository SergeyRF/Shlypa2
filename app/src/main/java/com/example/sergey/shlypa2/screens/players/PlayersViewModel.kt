package com.example.sergey.shlypa2.screens.players

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.ImagesHelper
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.data.PlayersRepository
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.extensions.random
import com.example.sergey.shlypa2.game.PlayerType
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.anal.AnalSender
import com.example.sergey.shlypa2.utils.coroutines.CoroutineAndroidViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by alex on 3/31/18.
 */
class PlayersViewModel(application: Application,
                       private val dataProvider: DataProvider,
                       private val dispatchers: DispatchersProvider,
                       private val playersRepository: PlayersRepository,
                       private val anal: AnalSender)
    : CoroutineAndroidViewModel(dispatchers.uiDispatcher, application) {

    private val playersLiveData = MutableLiveData<List<Player>>()
    private val teamsLiveData = MutableLiveData<List<Team>>()
    val avatarLiveData = MutableLiveData<String>()
    val toastResLD = MutableLiveData<Int>()

    val commandLiveData = SingleLiveEvent<Command>()
    val titleLiveData = MutableLiveData<Int>()

    val listOfAvatars: MutableList<String> = mutableListOf()
    val listOfUserPlayers = mutableListOf<Player>()

    var playerImage: String? = null


    init {
        updateData()
        loadAvatars()
    }

    fun getPlayersLiveData(): LiveData<List<Player>> = playersLiveData

    fun getTeamsLiveData(): LiveData<List<Team>> = teamsLiveData

    fun removePlayer(player: Player) {
        playersRepository.removePlayer(player)
        updateData()
    }

    fun reNamePlayer(player: Player) {
        launch(dispatchers.ioDispatcher) {
            if (player.type == PlayerType.USER) {
                playersRepository.reNamePlayer(player)
            } else {
                playersRepository.removePlayer(player)
                //set id to zero before inserting
                player.type = PlayerType.USER
                player.id = 0
                player.id = dataProvider.insertPlayer(player)
                playersRepository.addPlayer(player)
            }
        }
    }

    fun addRandomPlayer() {
        launch {
            withContext(dispatchers.ioDispatcher) {
                val currentPlayers = playersRepository.getPlayers()
                dataProvider.getPlayers()
                        .find { !currentPlayers.contains(it) }
                        ?.let { playersRepository.addPlayer(it) }
            }

            updateData()
            anal.playerAdded(true)
        }
    }

    fun addImage(image: Uri) {
        launch {
            withContext(dispatchers.ioDispatcher) {
                playerImage = ImagesHelper.saveImage(image, getApplication())
            }
        }
    }

    fun addImage(image: String) {
        playerImage = image
    }

    fun addPlayer(name: String) {
        if (name.isBlank()) {
            toastResLD.value = R.string.player_name_empty
            return
        }

        launch {
            val success = withContext(dispatchers.ioDispatcher) {
                val player = Player(name,
                        avatar = playerImage ?: avatarLiveData.value ?: "",
                        type = PlayerType.USER
                )
                player.id = dataProvider.insertPlayer(player)
                playersRepository.addPlayer(player)
            }

            if (success) {
                avatarLiveData.value = listOfAvatars.random()
                updateData()
            } else {
                toastResLD.value = R.string.name_not_unic
            }
            anal.playerAdded(false)
        }
    }

    fun addPlayer(player: Player) {
        launch {
            val success = withContext(dispatchers.ioDispatcher) {
                playersRepository.addPlayer(player)
            }
            if (success) {
                updateData()
            } else {
                toastResLD.value = R.string.name_not_unic
            }
            anal.playerAddedFromSaved()
        }
    }

    private fun loadAvatars() = launch {
        withContext(dispatchers.ioDispatcher) {
            listOfAvatars.addAll(dataProvider.getListOfAvatars())
            listOfUserPlayers.addAll(dataProvider.getPlayersUser())
        }

        avatarLiveData.value = listOfAvatars.random()
    }

    fun initTeams() {
        playersRepository.createTeams(2)
        updateData()
    }

    fun addTeam() {
        val teamsCount = playersRepository.getTeams().size + 1
        if (teamsCount <= playersRepository.maxTeamsCount) {
            playersRepository.createTeams(teamsCount)
            updateData()
        } else {
            Toast.makeText(getApplication(), R.string.cant_create_teams, Toast.LENGTH_SHORT).show()
        }
    }

    fun reduceTeam() {
        if (playersRepository.getTeams().size > 2) {
            val teamsCount = playersRepository.getTeams().size - 1
            playersRepository.createTeams(teamsCount)
            updateData()
        } else {
            Toast.makeText(getApplication(), R.string.two_team_min, Toast.LENGTH_SHORT).show()
        }
    }

    fun shuffleTeams() {
        launch {
            withContext(dispatchers.ioDispatcher) {
                playersRepository.createTeams(playersRepository.getTeams().size)
            }
            updateData()
        }
    }

    private fun updateData() {
        playersLiveData.value = playersRepository.getPlayers()
        teamsLiveData.value = playersRepository.getTeams()
    }

    fun onPlayersNextClicked() {
        if (playersRepository.getPlayers().size < 4) {
            toastResLD.value = R.string.not_enough_players
        } else {
            commandLiveData.value = Command.START_TEAMS
        }
    }

    fun setTitleId(resourceId: Int) {
        titleLiveData.value = resourceId
    }

    fun startSettings() {
        commandLiveData.value = Command.START_SETTINGS
        anal.sendEventTeamsCreated(playersRepository.getPlayers().size, playersRepository.getTeams().size)
    }

    enum class Command {
        START_TEAMS,
        START_SETTINGS
    }
}