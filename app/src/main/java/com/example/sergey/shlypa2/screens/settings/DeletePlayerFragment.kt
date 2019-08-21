package com.example.sergey.shlypa2.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.show
import com.example.sergey.shlypa2.screens.settings.adapter.ItemPlayerDelete
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.fragment_delete_player.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeletePlayerFragment : Fragment(),
        FlexibleAdapter.OnItemSwipeListener {


    val viewModel: SettingsViewModel by viewModel()

    private val playersAdapter = FlexibleAdapter(emptyList(), this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_delete_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDeletedPlayers.layoutManager = LinearLayoutManager(requireContext())
        rvDeletedPlayers.adapter = playersAdapter
        playersAdapter.isSwipeEnabled = true

        viewModel.playersLiveData.observeSafe(this) {
            onPlayersChanged(it)
        }

    }

    private fun onPlayersChanged(players: List<Player>) {
        if (players.isEmpty()) {
            tvNotPlayer.show()
        }
        players.map {
            ItemPlayerDelete(it)
        }.apply {
            playersAdapter.updateDataSet(this)
        }
    }

    override fun onActionStateChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
    }

    override fun onItemSwipe(position: Int, direction: Int) {
        when (val item = playersAdapter.getItem(position)) {
            is ItemPlayerDelete -> {
                viewModel.removePlayer(item.player)
            }
            else -> {
            }
        }
    }
}