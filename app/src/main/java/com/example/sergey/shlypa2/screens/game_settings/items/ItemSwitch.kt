package com.example.sergey.shlypa2.screens.game_settings.items


import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.views.SwitchSetting
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder


class ItemSwitch(
        private val title: String,
        private val setChecked: Boolean,
        private val checkedListener: (Boolean) -> Unit
) : AbstractFlexibleItem<ItemSwitch.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {

        with(holder) {
            switch.setTitle(title)
                    .setChecked(setChecked)
                    .setOnCheckedListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
                        checkedListener.invoke(isChecked)
                    })
        }

    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemSwitch.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemSwitch

    override fun getLayoutRes() = R.layout.holder_switch

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {

        val switch: SwitchSetting = view.findViewById<SwitchSetting>(R.id.itemSwitch)
    }
}