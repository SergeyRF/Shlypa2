package com.example.sergey.shlypa2.screens.features_testing


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.onTransitionCompletedOnce
import com.example.sergey.shlypa2.utils.Functions
import com.flurry.sdk.t
import kotlinx.android.synthetic.main.activity_game_result.*
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.view.*
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

        //tv_winner.text = "Good guys"
        tvRoundName.text = "First round"
        tvRoundRules.text =
                """Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                    |Suspendisse nisl massa, scelerisque at ex congue, congue fringilla magna.
                    |Cras viverra enim non neque commodo pretium. Suspendisse dictum lectus libero,
                    |ut ultrices mi ornare non. Aliquam erat volutpat. Maecenas venenatis at sem ac vestibulum.
                    """.trimMargin()
        Glide.with(this)
                .load(Functions.imageNameToUrl("round_avatars/silence.png"))
                .into(civRulesAvatar)

        /*rootGame.setTransition(R.id.start, R.id.end)
        rootGame.transitionToEnd()
        rootGame.onTransitionCompletedOnce {
            tvGuideLabel.setText(R.string.skip)
            rootGame.setTransition(R.id.start, R.id.endSkip)
            rootGame.transitionToEnd()
        }*/
    }
}
