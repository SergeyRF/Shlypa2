package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.Player
import com.example.sergey.shlypa2.viewModel.PlayersViewModel

class PlayersActivity : AppCompatActivity() {

    lateinit var adapter: RvAdapter

    lateinit var viewModel: PlayersViewModel

    lateinit var rvPlayers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)
        val editName = findViewById<EditText>(R.id.EtName)
        val buttonName = findViewById<RadioButton>(R.id.radioButton)
        val buttonNext = findViewById<Button>(R.id.goNext)

        val linLayout = LinearLayoutManager(this)
        linLayout.stackFromEnd = true
        linLayout.reverseLayout = true

        rvPlayers = findViewById<RecyclerView>(R.id.list_playrs)
        rvPlayers.layoutManager = linLayout
        adapter = RvAdapter()
        rvPlayers.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        viewModel.getPlayersLiveData().observe(this, Observer { list -> onPlayersChanged(list) })

        createFakePlayers()

        buttonName.setOnClickListener {
            if (editName.text.isNotEmpty()) {
                if (viewModel.addPlayer(Player(editName.text.toString()))) {
                } else {
                    Toast.makeText(this, R.string.name_not_unic, Toast.LENGTH_LONG).show()
                }
                editName.setText("")
            } else Toast.makeText(this, R.string.player_name_empty, Toast.LENGTH_LONG).show()
        }

        buttonNext.setOnClickListener {
            if (Game.getPlayers().size < 4) {
                Toast.makeText(this, R.string.not_enough_players, Toast.LENGTH_LONG).show()
            } else startActivity(Intent(this, CommandActivity::class.java))
        }
    }

    private fun onPlayersChanged(players: List<Player>?) {
        adapter.setData(players)
        rvPlayers.scrollToPosition(0)
    }

    private fun createFakePlayers() {
        for (i in 0..10) {
            viewModel.addPlayer(Player("FAPPPP$i"))
        }
    }
}
