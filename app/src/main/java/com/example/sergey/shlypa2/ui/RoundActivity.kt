package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.db.DataProvider
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.ui.fragments.*
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import timber.log.Timber

class RoundActivity : AppCompatActivity() {

    lateinit var viewModel: RoundViewModel

    lateinit var dataProvider: DataProvider

    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setTheme(this)
        super.onCreate(savedInstanceState)

        supportActionBar?.elevation = 0F

        viewModel = ViewModelProviders.of(this).get(RoundViewModel::class.java)
        dataProvider = DataProvider(applicationContext)

        viewModel.commandCallback.observe(this, Observer { command ->
            when (command) {
                RoundViewModel.Command.GET_READY -> startGetReadyFragment()
                RoundViewModel.Command.START_TURN -> startGameFragment()
                RoundViewModel.Command.FINISH_TURN -> startTurnResultFragment()
                RoundViewModel.Command.SHOW_ROUND_RESULTS -> startRoundResultsFragment()
                RoundViewModel.Command.START_NEXT_ROUND -> startNextRound()
                RoundViewModel.Command.SHOW_GAME_RESULTS -> startGameResults()
                RoundViewModel.Command.EXIT -> finish()
            }
        })

        if (supportFragmentManager.findFragmentById(android.R.id.content) == null) {
            startStartFragment()
        }

        viewModel.roundLiveData.observe(this, Observer {
            it?.let { setTitle(it.name) }
        })
    }

    override fun onBackPressed() {
        if(backPressedOnce) {
            finish()
        } else {
            backPressedOnce = true
            Toast.makeText(this, R.string.press_more_to_finish_game, Toast.LENGTH_LONG).show()
            Handler().postDelayed({backPressedOnce = false}, 2000)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        viewModel.saveGameState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //TODO remove debug menu
        menuInflater.inflate(R.menu.debug_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_save_state_debug -> {
                dataProvider.insertState(Game.state)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startStartFragment() {
        val fragment = RoundStartFragment()
        startFragment(fragment, true)
    }

    private fun startGetReadyFragment() {
        val fragment = TurnStartFragment()
        startFragment(fragment)
    }

    private fun startGameFragment() {
        val fragment = GameFragment()
        startFragment(fragment)
    }

    private fun startTurnResultFragment() {
        val fragment = TurnResultFragment()
        startFragment(fragment)
    }

    private fun startRoundResultsFragment() {
        val fragment = RoundResultFragment()
        startFragment(fragment)
    }

    private fun startNextRound() {
        startActivity(Intent(this, RoundActivity::class.java))
        Timber.d("Start next round")
        finish()
    }

    private fun startGameResults() {
        startActivity(Intent(this, GameResultActivity::class.java))
        Timber.d("No more rounds ")
        finish()
    }

    private fun startFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(android.R.id.content, fragment)

        if (addToBackStack) transaction.addToBackStack(null)

        transaction.commit()
    }
}
