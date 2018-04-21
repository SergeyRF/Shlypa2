package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.viewModel.PlayersViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_players.*


/**
 * A simple [Fragment] subclass.
 */
class PlayersFragment : Fragment() {

    lateinit var adapter: RvAdapter
    lateinit var viewModel: PlayersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        viewModel = ViewModelProviders.of(activity!!).get(PlayersViewModel::class.java)

        return inflater.inflate(R.layout.fragment_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linLayout = LinearLayoutManager(context)
        linLayout.stackFromEnd = true
        linLayout.reverseLayout = true

        rvPlayers.layoutManager = linLayout
        adapter = RvAdapter()
        rvPlayers.adapter = adapter



        viewModel.getPlayersLiveData().observe(this, Observer { list -> onPlayersChanged(list) })

        adapter.listener= {player:Any ->
            //todo player saves incorrect
            viewModel.reNamePlayer(player as Player)
        }

        imageButton.setOnClickListener {
            if (etName.text.isNotEmpty()) {
                if (viewModel.addPlayer(Player(etName.text.toString().trim()))) {
                } else {
                    Toast.makeText(context, R.string.name_not_unic, Toast.LENGTH_LONG).show()
                }
                etName.setText("")
            } else Toast.makeText(context, R.string.player_name_empty, Toast.LENGTH_LONG).show()
        }

        btGoNext.setOnClickListener {
            if (Game.getPlayers().size < 4) {
                Toast.makeText(context, R.string.not_enough_players, Toast.LENGTH_LONG).show()
            } else viewModel.startTeams()
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

        /*Picasso.get()
                .load(viewModel.getRandomAvatar())
                .into(civPlayerAvatar)*/

    }

    override fun onStart() {
        super.onStart()
        viewModel.setTitleId(R.string.player_actyvity)
    }

    private fun onPlayersChanged(players: List<Player>?) {
        adapter.setData(players)
        val position = players?.size ?: 0
        rvPlayers.scrollToPosition(position - 1)
    }
}
