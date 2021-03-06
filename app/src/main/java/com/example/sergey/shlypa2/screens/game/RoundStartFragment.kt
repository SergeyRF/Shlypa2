package com.example.sergey.shlypa2.screens.game


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.ImagesHelper
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.observeSafe
import kotlinx.android.synthetic.main.fragment_round_start.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RoundStartFragment : Fragment() {

    private val viewModel: RoundViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_round_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.roundLiveData.observeSafe(this) { round ->
            tvRoundName.setText(round.descriptor.nameRes)
            tvRoundRules.setText(round.descriptor.rules)

            Glide.with(this)
                    .load(ImagesHelper.getRoundImage(round.descriptor.icon))
                    .into(civRulesAvatar)

        }

        btBeginRound.setOnClickListener { viewModel.beginRound() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.hint_team_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.item_show_hint -> {
                viewModel.showScoresTable()
                true
            }
            else -> false
        }
    }

}
