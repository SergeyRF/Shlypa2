package com.example.sergey.shlypa2.screens.game_settings.items


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.ItemPenaltySettings
import com.example.sergey.shlypa2.extensions.setVisibility
import com.example.sergey.shlypa2.views.SeekbarSetting
import com.example.sergey.shlypa2.views.SwitchSetting
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.holder_penalty.view.*


class ItemPenalty(
        private val penalty: ItemPenaltySettings,
        private val penaltyListener: (Boolean) -> Unit,
        private val progressListener: (Int) -> Unit
) : AbstractFlexibleItem<ItemPenalty.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {

        with(holder) {

            penaltySwitch
                    .setChecked(penalty.penaltyCheck)
                    .setOnCheckedListener { isChecked ->
                        penaltyListener.invoke(isChecked)
                        penaltySeekBar.setVisibility(isChecked)
                    }

            penaltySeekBar.setVisibility(penalty.penaltyCheck)
            penaltySeekBar.setValues(penalty.min, penalty.max)
                    .setProgress(penalty.progress)
                    .setProgressListener {
                        progressListener.invoke(it)
                    }
        }


    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemPenalty.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemPenalty

    override fun getLayoutRes() = R.layout.holder_penalty

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {

        val penaltySwitch: SwitchSetting = view.itemSwitchSettings
        val penaltySeekBar: SeekbarSetting = view.itemSeekBar
    }
}
