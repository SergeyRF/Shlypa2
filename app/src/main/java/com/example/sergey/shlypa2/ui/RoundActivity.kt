package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.ui.fragments.*
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import com.example.sergey.shlypa2.viewModel.StateViewModel
import timber.log.Timber

class RoundActivity : AppCompatActivity() {

    lateinit var viewModel: RoundViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //For debug only
        Game.settings.time = 10

        viewModel = ViewModelProviders.of(this).get(RoundViewModel::class.java)

        startStartFragment()
    }

    fun startStartFragment() {
        val fragment = RoundStartFragment()
        startFragment(fragment, true)
    }

    fun startGetReadyFragment() {
        val fragment = TurnStartFragment()
        startFragment(fragment)
    }

    fun startGameFragment() {
        val fragment = GameFragment()
        startFragment(fragment)
    }

    fun startTurnFinishFragment() {
        val fragment = TurnResultFragment()
        startFragment(fragment)
    }

    fun startFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)

        if (addToBackStack) transaction.addToBackStack(null)

        transaction.commit()
    }

    fun onTurnFinished() {
        var fragment: Fragment =
                if (viewModel.roundFinished) {
                    RoundResultFragment()
                } else {
                    TurnStartFragment()
                }

        startFragment(fragment)
    }

    fun onRoundFinish() {
        Game.beginNextRound()
        if(Game.hasRound()) {
            startActivity(Intent(this, RoundActivity::class.java))
            Timber.d("Start next round")
        } else {
            Timber.d("No more rounds ")
        }
        finish()
    }
}
