package com.example.sergey.shlypa2.screens.players.dialog

import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.data.PlayersRepository
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.utils.SingleLiveEvent
import com.example.sergey.shlypa2.utils.coroutines.CoroutineViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerSelectViewModel(
        private val dispatchers: DispatchersProvider,
        private val dataProvider: DataProvider,
        private val playersRepository: PlayersRepository) : CoroutineViewModel(dispatchers.uiDispatcher) {

    val playersLiveData = MutableLiveData<List<Player>>()
    val dismissLiveData = SingleLiveEvent<Boolean>()

    init {
       loadPlayers()
    }

    fun onPlayerSelected(player: Player) {
        playersRepository.addPlayer(player)
        dismissLiveData.value = true
    }

    private fun loadPlayers() = launch {
        val players = withContext(dispatchers.ioDispatcher) {
            val currentPlayers = playersRepository.getPlayers()
            dataProvider.getPlayersUser()
                    .filterNot { currentPlayers.contains(it) }
        }

        playersLiveData.value = players
    }
}