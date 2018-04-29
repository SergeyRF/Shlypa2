package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.ui.RoundActivity
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class RoundResultFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_round_result, container, false)

        val viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)

        val btNextRound : Button = root.findViewById(R.id.btNextRound)
        btNextRound.setOnClickListener{ viewModel.finishRound()}

        val tvRound: TextView = root.findViewById(R.id.tvRoundNumber)
        //todo fink about it
        //tvRound.setText(viewModel.roundDescription)
        tvRound.setText(R.string.round_end)

        val adapter = RvAdapter()

        viewModel.rounResultLiveData.observe(this, Observer {
            adapter.setData(it)
        })

        val rvTeams : RecyclerView = root.findViewById(R.id.rvRoundResult)
        rvTeams.layoutManager = LinearLayoutManager(context)
        rvTeams.adapter = adapter


        return root
    }


}
