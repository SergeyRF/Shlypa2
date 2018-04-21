package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ui.RoundActivity
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


/**
 * A simple [Fragment] subclass.
 */
class TurnStartFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_turn_start, container, false)

        val playerTv : TextView  = root.findViewById(R.id.tvTurnPlayerName)
        val startButton : Button = root.findViewById(R.id.btTurnStart)
        val playerAvatar : CircleImageView = root.findViewById(R.id.civPlayerAvatar)

        playerTv.text = viewModel.getPlayer().name
        startButton.setOnClickListener{viewModel.startTurn()}

        Picasso.get()
                .load(Functions.imageNameToUrl(viewModel.getPlayer().avatar))
                .into(playerAvatar)

        return root
    }

}
