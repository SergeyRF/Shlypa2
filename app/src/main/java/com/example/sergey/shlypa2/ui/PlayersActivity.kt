package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
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


        viewModel = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        viewModel.getPlayersLiveData().observe(this, Observer { list -> onPlayersChanged(list) })

        adapter.listener= {player:Any ->
            //todo player saves incorrect
            viewModel.reNamePlayer(player as Player)
        }
     /*  *//* adapter.listenerTwo={player:Any->
            viewModel.addPlayer(player as Player)*//*
//            adapter.notifyDataSetChanged()
        }*/


        imageButton.setOnClickListener {
            if (etName.text.isNotEmpty()) {
                if (viewModel.addPlayer(Player(etName.text.toString().trim()))) {
                } else {
                    Toast.makeText(this, R.string.name_not_unic, Toast.LENGTH_LONG).show()
                }
                etName.setText("")
            } else Toast.makeText(this, R.string.player_name_empty, Toast.LENGTH_LONG).show()
        }

        btGoNext.setOnClickListener {
            if (Game.getPlayers().size < 4) {
                Toast.makeText(this, R.string.not_enough_players, Toast.LENGTH_LONG).show()
            } else startActivity(Intent(this, TeamActivity::class.java))
        }

        btAddRandomPlayer.setOnClickListener{
            viewModel.addRandomPlayer()
        }



        //enter
        etName.setOnEditorActionListener { v, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_NEXT&&etName.text.isNotEmpty()) {
                // обработка нажатия Enter
                viewModel.addPlayer(Player(etName.text.toString().trim()))
                etName.text.clear()
                true
            } else true
        }
    }

    private fun onPlayersChanged(players: List<Player>?) {
        adapter.setData(players)
        val position = players?.size ?: 0
        rvPlayers.scrollToPosition(position - 1)
    }
}
