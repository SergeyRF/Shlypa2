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
import com.example.sergey.shlypa2.utils.hide
import com.example.sergey.shlypa2.utils.show
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


/**
 * A simple [Fragment] subclass.
 */
class RoundStartFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_round_start, container, false)
        val tvDescription : TextView = root.findViewById(R.id.tvRoundDescription)
        val tvRules : TextView = root.findViewById(R.id.tvRoundRules)
        val btGo : Button = root.findViewById(R.id.btBeginRound)
        val rulesAvatar:CircleImageView = root.findViewById(R.id.civRulesAvatar)
        tvRules.hide()

        val viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)
        tvDescription.setText( viewModel.roundName)
        tvRules.setText(viewModel.roundRules)

        btGo.setOnClickListener{viewModel.beginRound()}

        Picasso.get()
                .load(Functions.imageNameToUrl("round_avatars/${viewModel.roundImage}"))
                .into(rulesAvatar)
        rulesAvatar.setOnClickListener {
            tvRules.show()
        }
        return root
    }

}// Required empty public constructor
