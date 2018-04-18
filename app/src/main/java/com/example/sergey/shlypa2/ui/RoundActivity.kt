package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.ui.fragments.*
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import com.example.sergey.shlypa2.viewModel.StateViewModel
import timber.log.Timber

class RoundActivity : AppCompatActivity() {

    lateinit var viewModel: RoundViewModel

    lateinit var dataProvider : DataProvider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO Remove this debug shit
        Game.getSettings().time = 10

        viewModel = ViewModelProviders.of(this).get(RoundViewModel::class.java)
        dataProvider = DataProvider(applicationContext)

        startStartFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.debug_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.item_save_state_debug -> {
                dataProvider.insertState(Game.state)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
            startActivity(Intent(this, GameResultActivity::class.java))
            Timber.d("No more rounds ")
        }
        finish()
    }
}
