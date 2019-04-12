package com.example.sergey.shlypa2.screens.players

import android.content.Context
import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.game_settings.GameSettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayersActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, PlayersActivity::class.java)
    }

    lateinit var adapter: RvAdapter

    val viewModel by viewModel<PlayersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)

        viewModel.commandLiveData.observe(this, Observer { command ->
            if (command != null) onCommand(command)
        })

        viewModel.titleLiveData.observe(this, Observer { titleId ->
            if(titleId != null) setTitle(titleId)
        })

        viewModel.toastResLD.observeSafe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            startPlayersFragment()
        }

        //val color = Functions.getThemedBgColor(this, R.color.)
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
                .replace(R.id.container, fragment)
                .commit()
    }

    private fun startTeamsFragment() {
        val fragment = TeamsFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    private fun startSettings() {
        startActivity(Intent(this, GameSettingsActivity::class.java))
    }

}
