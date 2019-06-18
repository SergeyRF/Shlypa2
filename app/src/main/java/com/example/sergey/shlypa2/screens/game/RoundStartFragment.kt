package com.example.sergey.shlypa2.screens.game


import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.Functions
import de.hdodenhof.circleimageview.CircleImageView


/**
 * A simple [Fragment] subclass.
 */
class RoundStartFragment : androidx.fragment.app.Fragment() {

    lateinit var viewModel: RoundViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_round_start, container, false)
        val tvRoundName: TextView = root.findViewById(R.id.tvRoundName)
        val tvRules: TextView = root.findViewById(R.id.tvRoundRules)
        val btGo: Button = root.findViewById(R.id.btBeginRound)
        val rulesAvatar: CircleImageView = root.findViewById(R.id.civRulesAvatar)

        viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)

        viewModel.roundLiveData.observe(this, Observer {
            it?.let {
                tvRoundName.setText(it.name)
                tvRules.setText(it.rules)

                Glide.with(this)
                        .load(Functions.imageNameToUrl("round_avatars/${it.image}"))
                        .into(rulesAvatar)
            }
        })

        btGo.setOnClickListener { viewModel.beginRound() }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.hint_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.item_show_hint) {
            viewModel.loadHintTeam()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}// Required empty public constructor
