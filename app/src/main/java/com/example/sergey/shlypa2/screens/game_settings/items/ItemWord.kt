package com.example.sergey.shlypa2.screens.game_settings.items


import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.ItemWordSettings
import com.example.sergey.shlypa2.beans.Type
import com.example.sergey.shlypa2.extensions.setVisibility
import com.example.sergey.shlypa2.utils.TypesArrayAdapter
import com.example.sergey.shlypa2.views.SwitchSetting
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.holder_word_settings.view.*


class ItemWord(
        private val word: ItemWordSettings,
        private val listenerAutoFill: (Boolean) -> Unit,
        private val listenerAllowRandom: (Boolean) -> Unit,
        private val typeListener: (Type) -> Unit
) : AbstractFlexibleItem<ItemWord.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {

        with(holder) {

            switchAllWordRandom.setChecked(word.autoFill)
                    .setOnCheckedListener { isChecked ->
                        listenerAutoFill.invoke(isChecked)
                        switchAllowRandom.setVisibility(!isChecked)
                        if (isChecked) viewDifficult.setVisibility(true)
                        else viewDifficult.setVisibility(switchAllowRandom.isChecked())
                    }

            switchAllowRandom.setChecked(word.allowRandom)
                    .setOnCheckedListener { isChecked ->
                        listenerAllowRandom.invoke(isChecked)
                        viewDifficult.setVisibility(isChecked)
                    }
                    .setVisibility(!switchAllWordRandom.isChecked())

            viewDifficult.setVisibility(switchAllWordRandom.isChecked() ||
                    switchAllowRandom.isChecked() && switchAllowRandom.visibility == View.VISIBLE)
            val typesAdapter = TypesArrayAdapter(
                    itemView.context,
                    android.R.layout.simple_list_item_1,
                    word.typesList.toTypedArray())
            difficultSpinner.adapter = typesAdapter

            difficultSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    typeListener.invoke(word.typesList[position])
                }
            }

            (difficultSpinner.adapter as? TypesArrayAdapter)?.getPosition(word.selectedType)
                    ?.let { difficultSpinner.setSelection(it) }

        }

    }

    override fun createViewHolder(
            view: View,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemWord.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemWord

    override fun getLayoutRes() = R.layout.holder_word_settings

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {

        val switchAllWordRandom: SwitchSetting = view.itemSwitchSettings
        val switchAllowRandom: SwitchSetting = view.itemAllowRandom
        val viewDifficult: Group = view.viewDifficultSub
        val difficultSpinner: Spinner = view.spinnerDifficult
    }

}


