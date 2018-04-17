package com.example.sergey.shlypa2.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.GameState

/**
 * Created by alex on 4/17/18.
 */
class WelcomeViewModel(application: Application) : AndroidViewModel(application) {

    val dataProvider = DataProvider(application)

    fun getSavedStates() : List<GameState> {
        return dataProvider.getSavedStates()
    }
}