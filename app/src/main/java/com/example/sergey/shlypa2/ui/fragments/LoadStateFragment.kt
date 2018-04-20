package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.ui.RoundActivity
import com.example.sergey.shlypa2.viewModel.WelcomeViewModel
import kotlinx.android.synthetic.main.fragment_load_state.*


/**
 * A simple [Fragment] subclass.
 */
class LoadStateFragment : Fragment() {

    lateinit var viewModel : WelcomeViewModel
    lateinit var adapter: RvAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(WelcomeViewModel::class.java)

        adapter = RvAdapter()
        adapter.listener = {state : Any ->
            Game.state = state as GameState
            startActivity(Intent(context, RoundActivity::class.java))
        }

        return inflater!!.inflate(R.layout.fragment_load_state, container, false)
    }

    override fun onStart() {
        super.onStart()
        rvStates.layoutManager = LinearLayoutManager(context)
        rvStates.adapter = adapter
        adapter.setData(viewModel.getSavedStates())
    }
}
