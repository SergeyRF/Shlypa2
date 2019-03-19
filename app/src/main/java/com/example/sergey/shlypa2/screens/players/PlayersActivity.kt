package com.example.sergey.shlypa2.screens.players

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.ui.GameSettingsActivity
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.PlayersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayersActivity : AppCompatActivity() {

    lateinit var adapter: RvAdapter

    val viewModel by viewModel<PlayersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setThemeApi21(this)
        super.onCreate(savedInstanceState)

        viewModel.commandLiveData.observe(this, Observer { command ->
            if (command != null) onCommand(command)
        })

        viewModel.titleLiveData.observe(this, Observer { titleId ->
            if(titleId != null) setTitle(titleId)
        })

        viewModel.toastResLD.observeSafe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

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
