package com.example.sergey.shlypa2.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.ui.RoundActivity


/**
 * A simple [Fragment] subclass.
 */
class RoundResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_round_result, container, false)

        val btNextRound : Button = root.findViewById(R.id.btNextRound)
        btNextRound.setOnClickListener{(activity as RoundActivity).onRoundFinish()}

        val adapter = RvAdapter()
        adapter.setData(Game.getTeams())

        val rvTeams : RecyclerView = root.findViewById(R.id.rvRoundResult)
        rvTeams.layoutManager = LinearLayoutManager(context)
        rvTeams.adapter = adapter

        return root
    }

}
