package com.example.sergey.shlypa2.screens.players.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.screens.players.adapter.ItemPlayerSelect
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.dialog_player_select.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectPlayerDialogFragment : DialogFragment(), FlexibleAdapter.OnItemClickListener {

    private val viewModel by viewModel<PlayerSelectViewModel>()

    val playersAdapter = FlexibleAdapter(emptyList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.CustomDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(
            R.layout.dialog_player_select, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(true)

        with(rvPlayersDialog) {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = playersAdapter
        }

        viewModel.playersLiveData.observeSafe(this) { players ->
            playersAdapter.updateDataSet(
                    players.map { ItemPlayerSelect(it) }
            )
        }

        viewModel.dismissLiveData.observeSafe(this) { dismissAllowingStateLoss() }
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        return when (val item = playersAdapter.getItem(position)) {
            is ItemPlayerSelect -> {
                viewModel.onPlayerSelected(item.player)
                true
            }
            else -> false
        }
    }
}