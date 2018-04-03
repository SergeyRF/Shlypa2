package com.example.sergey.shlypa2.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.Round

/**
 * Created by alex on 4/3/18.
 */
class RoundViewModel : ViewModel() {

    val round = Game.getRound()

    var roundDescription = round.description
    var roundRules = round.rules

    val playerName = MutableLiveData<String>()


}