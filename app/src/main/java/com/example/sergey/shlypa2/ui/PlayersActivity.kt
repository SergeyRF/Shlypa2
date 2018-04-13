package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.widget.Toast
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.viewModel.PlayersViewModel
import kotlinx.android.synthetic.main.activity_players.*

class PlayersActivity : AppCompatActivity() {

    lateinit var adapter: RvAdapter

    lateinit var viewModel: PlayersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val linLayout = LinearLayoutManager(this)
        linLayout.stackFromEnd = true
        linLayout.reverseLayout = true

        rvPlayers.layoutManager = linLayout
        adapter = RvAdapter()
        rvPlayers.adapter = adapter
        adapter.listener= {player:Any ->
            etName.setText( (player as Player).name)

        }

        viewModel = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        viewModel.getPlayersLiveData().observe(this, Observer { list -> onPlayersChanged(list) })

        imageButton.setOnClickListener {
            if (etName.text.isNotEmpty()) {
                if (viewModel.addPlayer(Player(etName.text.toString()))) {
                } else {
                    Toast.makeText(this, R.string.name_not_unic, Toast.LENGTH_LONG).show()
                }
                etName.setText("")
            } else Toast.makeText(this, R.string.player_name_empty, Toast.LENGTH_LONG).show()
        }

        btGoNext.setOnClickListener {
            if (Game.getPlayers().size < 4) {
                Toast.makeText(this, R.string.not_enough_players, Toast.LENGTH_LONG).show()
            } else startActivity(Intent(this, GameSettingsActivity::class.java))
        }

        btAddRandomPlayer.setOnClickListener{
            viewModel.addRandomPlayer()
        }
    }

    private fun onPlayersChanged(players: List<Player>?) {
        adapter.setData(players)
        val position = players?.size ?: 0
        rvPlayers.scrollToPosition(position - 1)
    }
}
