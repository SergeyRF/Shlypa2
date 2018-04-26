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
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.hide
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import com.github.florent37.kotlin.pleaseanimate.please
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_round_start.*


/**
 * A simple [Fragment] subclass.
 */
class RoundStartFragment : Fragment() {

    val animation by lazy {
        please {
            animate(tvRoundDescription) {
                topOfHisParent(16f)
                leftOfHisParent(16f)
            }

            animate(civRulesAvatar) {
                topOfHisParent(16f)
                rightOfHisParent(16f)
                scale(0.3f, 0.3f)
            }

            animate(tvRoundRules) {
                belowOf(civRulesAvatar, 16f)
                visible()
            }
        }
    }

    var animated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_round_start, container, false)
        val tvDescription: TextView = root.findViewById(R.id.tvRoundDescription)
        val tvRules: TextView = root.findViewById(R.id.tvRoundRules)
        val btGo: Button = root.findViewById(R.id.btBeginRound)
        val rulesAvatar: CircleImageView = root.findViewById(R.id.civRulesAvatar)


        val viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)
        tvDescription.setText(viewModel.roundName)
        tvRules.setText(viewModel.roundRules)

        btGo.setOnClickListener { viewModel.beginRound() }

        Picasso.get()
                .load(Functions.imageNameToUrl("round_avatars/${viewModel.roundImage}"))
                .into(rulesAvatar)

        rulesAvatar.setOnClickListener {
            animated = if (animated) {
                animation.reset()
                false
            } else {
                animation.start()
                true
            }
        }

        //an animation doesn't work if we hide view manually,
        //hide it by animation is a workaround
        please(3) {
            animate(tvRules) {
                invisible()
            }
        }.start()

        return root
    }

}// Required empty public constructor
