package com.example.sergey.shlypa2.screens.game


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.screens.game.adapter.ItemTeamWithScores
import com.example.sergey.shlypa2.utils.PrecaheLayoutManager
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.fragment_round_result.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RoundResultFragment : Fragment() {

    private val viewModel: RoundViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_round_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btNextRound.setOnClickListener { viewModel.finishRound() }
        tvRoundNumber.setText(R.string.round_end)

        val adapter = FlexibleAdapter(emptyList())
        rvRoundResult.layoutManager = PrecaheLayoutManager(context)
        rvRoundResult.adapter = adapter

        val sharedViewPool = RecyclerView.RecycledViewPool()
        viewModel.rounResultLiveData.observeSafe(this) { teams ->
            adapter.updateDataSet(
                    teams.map {
                        ItemTeamWithScores(it, sharedViewPool)
                    })
        }
    }
}
