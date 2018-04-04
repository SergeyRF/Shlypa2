package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.ViewModelProviders
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
import com.example.sergey.shlypa2.ui.RoundActivity
import com.example.sergey.shlypa2.viewModel.RoundViewModel


/**
 * A simple [Fragment] subclass.
 */
class TurnResultFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewModel = ViewModelProviders.of(activity).get(RoundViewModel::class.java)
        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_turn_result, container, false)

        val adapter = RvAdapter()
        adapter.setData(viewModel.getTurnResults())

        val rv : RecyclerView = root.findViewById(R.id.rvWordsTurnResult)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter

        val finishBt : Button = root.findViewById(R.id.btFinishTurn)

        finishBt.setOnClickListener{ viewModel.finishTurn()
            (activity as RoundActivity).onTurnFinished() }


        return root
    }

}
