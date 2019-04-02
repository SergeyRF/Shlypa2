package com.example.sergey.shlypa2.ui.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.utils.PrecaheLayoutManager
import com.example.sergey.shlypa2.screens.game.RoundViewModel


/**
 * A simple [Fragment] subclass.
 */
class RoundResultFragment : androidx.fragment.app.Fragment() {


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

        val rvTeams : androidx.recyclerview.widget.RecyclerView = root.findViewById(R.id.rvRoundResult)
        rvTeams.layoutManager = PrecaheLayoutManager(context)
        rvTeams.adapter = adapter


        return root
    }


}
