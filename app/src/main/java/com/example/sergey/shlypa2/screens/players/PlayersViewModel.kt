package com.example.sergey.shlypa2.screens.players

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.ImagesHelper
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.data.PlayersRepository
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.extensions.random
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.anal.AnalSender
import com.example.sergey.shlypa2.utils.coroutines.CoroutineAndroidViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import com.example.sergey.shlypa2.utils.coroutines.SerialJob
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

    val playersLiveData = playersRepository.getPlayersLiveData()
    val teamsLiveData = playersRepository.getTeamsLiveData()
    val teamRenameLiveData = SingleLiveEvent<Team>()
    val avatarLiveData = MutableLiveData<String>()
    val toastResLD = MutableLiveData<Int>()

    val commandLiveData = SingleLiveEvent<Command>()
    val titleLiveData = MutableLiveData<Int>()

    val listOfAvatars: MutableList<String> = mutableListOf()

    var playerImage: String? = null
    var addPlayerJob = SerialJob(this)

    init {
        loadAvatars()
    }

    fun removePlayer(player: Player) {
        playersRepository.removePlayer(player)
    }

    fun renamePlayer(newName:String, id:Long) {
        launch(dispatchers.ioDispatcher) {
            playersRepository.reNamePlayer(newName, id)
        }
    }
    fun onClickPlayer(player: Player){
        commandLiveData.value = Command.ShowPlayerRenameDialog(player)
    }

    fun onTeamClicked(team: Team) {
        commandLiveData.value = Command.ShowTeamRenameDialog(team)
    }

    fun renameTeam(newName: String, oldName: String) {
        playersRepository.renameTeam(newName, oldName)
    }

    fun addRandomPlayer() {
        addPlayerJob.offer {
            withContext(dispatchers.ioDispatcher) {
                playersRepository.addRandomPlayer()
            }

            anal.playerAdded(true)
        }
    }

    fun onChangeAvatarClicked() {
        commandLiveData.value = Command.ShowSelectAvatarDialog
    }

    fun addImage(image: Uri) {
        launch {
            withContext(dispatchers.ioDispatcher) {
                playerImage = ImagesHelper.saveImage(image, getApplication())
                avatarLiveData.postValue(playerImage)
            }
        }
    }

    fun addImage(image: String) {
        playerImage = image
        avatarLiveData.value = image
    }

    fun addNewPlayer(name: String) {
        if (name.isBlank()) {
            toastResLD.value = R.string.player_name_empty
            return
        }

        addPlayerJob.offer {
            val success = withContext(dispatchers.ioDispatcher) {
                playersRepository.addNewPlayer(name, avatarLiveData.value ?: "")
            }

            if (success) {
                avatarLiveData.value = listOfAvatars.random()
            } else {
                toastResLD.value = R.string.name_not_unic
            }

            anal.playerAdded(false)
        }
    }

    private fun loadAvatars() = launch {
        withContext(dispatchers.ioDispatcher) {
            listOfAvatars.addAll(dataProvider.getListOfAvatars())
        }

        avatarLiveData.value = listOfAvatars.random()
    }

    fun initTeams() = launch(dispatchers.ioDispatcher) {
        playersRepository.initTeams()
    }

    fun addTeam() = launch {
        val success = withContext(dispatchers.ioDispatcher) {
            playersRepository.incrementTeams()
        }

        if (!success) toastResLD.value = R.string.cant_create_teams
    }


    fun reduceTeam() = launch {
        val success = withContext(dispatchers.ioDispatcher) {
            playersRepository.reduceTeams()
        }
        if (!success) toastResLD.value = R.string.two_team_min
    }


    fun shuffleTeams() = launch(dispatchers.ioDispatcher) {
        playersRepository.shuffleTeams()
    }

    fun onPlayersNextClicked() {
        if (playersRepository.playersList.size < 4) {
            toastResLD.value = R.string.not_enough_players
        } else {
            commandLiveData.value = Command.StartTeams
        }
    }

    fun onAddFromSavedClicked() = launch {
        val hasSaved = withContext(dispatchers.ioDispatcher) {
            playersRepository.hasGoodUserPlayers()
        }

        if (hasSaved) {
            commandLiveData.value = Command.ShowSelectPlayerDialog
        } else {
            toastResLD.value = R.string.not_saved_players
        }
    }


    fun setTitleId(resourceId: Int) {
        titleLiveData.value = resourceId
    }

    fun saveTeamsAndStartSettings(teams: List<Team>) {
        if (saveTeams(teams)) startSettings()
    }

    private fun startSettings() {
        commandLiveData.value = Command.StartSettings
        anal.sendEventTeamsCreated(playersRepository.playersList.size, playersRepository.getTeams().size)
    }

    private fun saveTeams(teams: List<Team>): Boolean {
        teams.firstOrNull { it.players.size < 2 }
                ?.let {
                    toastResLD.value = R.string.teams_need_at_least_two
                    return false
                }

        playersRepository.changeTeams(teams)
        return true
    }

    fun onBackPressed() {
        playersRepository.clear()
    }

    sealed class Command {
        object StartTeams: Command()
        object StartSettings: Command()
        object ShowSelectPlayerDialog: Command()
        object ShowSelectAvatarDialog: Command()
        class ShowTeamRenameDialog(val team: Team): Command()
        class ShowPlayerRenameDialog(val player: Player): Command()
    }
}