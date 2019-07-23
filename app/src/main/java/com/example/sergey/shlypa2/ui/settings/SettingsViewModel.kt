package com.example.sergey.shlypa2.ui.settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.utils.coroutines.CoroutineAndroidViewModel
import com.example.sergey.shlypa2.utils.coroutines.DispatchersProvider
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application,
                        private val dataProvider: DataProvider,
                        private val dispatchers: DispatchersProvider) :
        CoroutineAndroidViewModel(dispatchers.uiDispatcher, application) {

    val playersLiveData = MutableLiveData<List<Player>>()

    init {
        updateData()
    }

    fun removePlayer(player: Player) {
        launch(dispatchers.ioDispatcher) {
            dataProvider.deletePlayer(player)
            updateData()
        }
    }

    private fun updateData() {
        launch(dispatchers.ioDispatcher) {
            playersLiveData.postValue(dataProvider.getPlayersUser())
        }
    }
}