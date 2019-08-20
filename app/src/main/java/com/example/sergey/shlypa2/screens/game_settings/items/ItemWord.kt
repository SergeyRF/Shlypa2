package com.example.sergey.shlypa2.screens.game_settings.items


import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.TypesArrayAdapter
import com.example.sergey.shlypa2.beans.Type
import com.example.sergey.shlypa2.extensions.setVisibility
import com.example.sergey.shlypa2.views.SwitchSetting
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder


class ItemWord(
        private val word: WordItemSettings
) : AbstractFlexibleItem<ItemWord.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {

        with(holder) {

            switchAllWordRandom.setChecked(word.autoFill)
                    .setOnCheckedListener { isChecked ->
                        word.listenerAutoFill.invoke(isChecked)
                        switchAllowRandom.setVisibility(!isChecked)
                        if (isChecked) viewDifficult.setVisibility(false)
                        else viewDifficult.setVisibility(switchAllowRandom.isChecked())
                    }

            switchAllowRandom.setChecked(word.allowRandom)
                    .setOnCheckedListener { isChecked ->
                        word.listenerAllowRandom.invoke(isChecked)
                        viewDifficult.setVisibility(isChecked)
                    }
                    .setVisibility(!switchAllWordRandom.isChecked())

            viewDifficult.setVisibility(switchAllowRandom.isChecked()
                    && switchAllowRandom.visibility == View.VISIBLE)
            val typesAdapter = TypesArrayAdapter(
                    itemView.context,
                    android.R.layout.simple_list_item_1,
                    word.typesList.toTypedArray())
            difficultSpinner.adapter = typesAdapter

            difficultSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    word.typeListener.invoke(word.typesList[position])
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

        val switchAllWordRandom: SwitchSetting = view.findViewById(R.id.itemSwitchSettings)
        val switchAllowRandom: SwitchSetting = view.findViewById(R.id.itemAllowRandom)
        val viewDifficult: Group = view.findViewById(R.id.viewDifficultSub)
        val difficultSpinner: Spinner = view.findViewById(R.id.spinnerDifficult)

    }


}

class WordItemSettings(
        val autoFill: Boolean,
        val listenerAutoFill: (Boolean) -> Unit,
        val allowRandom: Boolean,
        val listenerAllowRandom: (Boolean) -> Unit,
        val typesList: List<Type>,
        val selectedType: Type,
        val typeListener: (Type) -> Unit
)

