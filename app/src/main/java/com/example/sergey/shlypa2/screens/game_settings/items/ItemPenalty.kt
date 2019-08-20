package com.example.sergey.shlypa2.screens.game_settings.items


import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.setVisibility
import com.example.sergey.shlypa2.views.SeekbarSetting
import com.example.sergey.shlypa2.views.SwitchSetting
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder


class ItemPenalty(private val penalty: ItemPenaltySettings) : AbstractFlexibleItem<ItemPenalty.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {

        with(holder) {

            penaltySwitch
                    .setChecked(penalty.penaltyCheck)
                    .setOnCheckedListener { isChecked ->
                        penalty.penaltyListener.invoke(isChecked)
                        seekBarView.setVisibility(isChecked)
                    }

            seekBarView.setVisibility(penalty.penaltyCheck)
            penaltySeekBar.setValues(penalty.min, penalty.max)
                    .setProgress(penalty.progress)
                    .setProgressListener {
                        penalty.progressListener.invoke(it)
                    }
        }


    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemPenalty.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemPenalty

    override fun getLayoutRes() = R.layout.holder_penalty

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {

        val penaltySwitch: SwitchSetting = view.findViewById(R.id.itemSwitchSettings)
        val penaltySeekBar: SeekbarSetting = view.findViewById(R.id.itemSeekBar)
        val seekBarView: Group = view.findViewById(R.id.viewSeekBar)
    }
}

class ItemPenaltySettings(
        val penaltyCheck: Boolean,
        val penaltyListener: (Boolean) -> Unit,
        val min: Int,
        val max: Int,
        val progress: Int,
        val progressListener: (Int) -> Unit
)