package com.example.sergey.shlypa2.ui.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import kotlinx.android.synthetic.main.fragment_team_hint.*


class TeamHintFragment : Fragment() {


    val adapter = RvAdapter()

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/

    lateinit var viewModel: RoundViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_hint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)

        adapter.setData(viewModel.loadCurrrentBal())
        rvHintTeam_and_Result.layoutManager = LinearLayoutManager(view.context)
        rvHintTeam_and_Result.adapter = adapter

    }

}
