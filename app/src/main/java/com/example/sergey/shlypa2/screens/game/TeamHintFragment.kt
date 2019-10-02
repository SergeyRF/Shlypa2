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
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.fragment_team_hint.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class TeamHintFragment : Fragment() {


    private val adapter = FlexibleAdapter(emptyList())
    private val viewModel: RoundViewModel by sharedViewModel()
    private val playersViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team_hint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvScoresSheet.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(view.context)
        rvScoresSheet.adapter = adapter

        viewModel.updateCurrentScoresSheet()
        viewModel.scoresSheetLiveData.observeSafe(this) { teams ->
            adapter.updateDataSet(teams.map { ItemTeamWithScores(it, playersViewPool) })
        }
    }

}
