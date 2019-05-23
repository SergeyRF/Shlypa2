package com.example.sergey.shlypa2.screens.features_testing


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.Functions
import kotlinx.android.synthetic.main.fragment_round_start.*
import kotlinx.android.synthetic.main.fragment_rules.*

import org.koin.androidx.viewmodel.ext.android.viewModel


class MotionTestFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_round_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvRoundName.text = "First round"
        tvRoundRules.text = "SOme rules"
        Glide.with(this)
                .load(Functions.imageNameToUrl("round_avatars/silence.png"))
                .into(civRulesAvatar)
    }
}
