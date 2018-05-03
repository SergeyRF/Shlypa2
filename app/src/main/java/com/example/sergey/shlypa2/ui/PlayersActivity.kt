package com.example.sergey.shlypa2.ui

import android.app.FragmentTransaction
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.ui.fragments.PlayersFragment
import com.example.sergey.shlypa2.ui.fragments.TeamsFragment
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.PlayersViewModel

class PlayersActivity : AppCompatActivity() {

    lateinit var adapter: RvAdapter

    lateinit var viewModel: PlayersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setTheme(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        viewModel.commandLiveData.observe(this, Observer { command ->
            if (command != null) onCommand(command)
        })

        viewModel.titleLiveData.observe(this, Observer { titleId ->
            if(titleId != null) setTitle(titleId)
        })

        if (supportFragmentManager.findFragmentById(android.R.id.content) == null) {
            startPlayersFragment()
        }
    }

    private fun onCommand(command: PlayersViewModel.Command) {
        when (command) {
            PlayersViewModel.Command.START_SETTINGS -> startSettings()
            PlayersViewModel.Command.START_TEAMS -> startTeamsFragment()
        }
    }

    private fun startPlayersFragment() {
        val fragment = PlayersFragment()
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit()
    }

    private fun startTeamsFragment() {
        val fragment = TeamsFragment()
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    private fun startSettings() {
        startActivity(Intent(this, GameSettingsActivity::class.java))
    }

}
