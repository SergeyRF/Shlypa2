package com.example.sergey.shlypa2.ui.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.ui.RoundActivity
import com.example.sergey.shlypa2.utils.show
import com.example.sergey.shlypa2.viewModel.WelcomeViewModel
import kotlinx.android.synthetic.main.fragment_load_state.*


/**
 * A simple [Fragment] subclass.
 */
class LoadStateFragment : androidx.fragment.app.Fragment() {

    lateinit var viewModel : WelcomeViewModel
    lateinit var adapter: RvAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(WelcomeViewModel::class.java)

        adapter = RvAdapter()
        adapter.listener = {state : Any ->

            val loadedState = state as GameState
            loadedState.needToRestore = true
            Game.state = loadedState

            startActivity(Intent(context, RoundActivity::class.java))
        }

        return inflater.inflate(R.layout.fragment_load_state, container, false)
    }

    override fun onStart() {
        super.onStart()
        rvStates.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rvStates.adapter = adapter

        viewModel.getSavedStates().observe(this, Observer {
            if(it == null || it.isEmpty()) {
                tvNoSavedGames.show()
            }else {
                adapter.setData(it)
            }
        })
    }
}
