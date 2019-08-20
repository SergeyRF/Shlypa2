package com.example.sergey.shlypa2.screens.game_settings.items


import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.TypesArrayAdapter
import com.example.sergey.shlypa2.beans.Type
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractExpandableHeaderItem
import eu.davidea.flexibleadapter.items.AbstractExpandableItem
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder


class ItemSpinner(
        private val title: String,
        private val typesList: List<Type>,
        private val selectedType: Type,
        private val typeListener: (Type) -> Unit
) : AbstractFlexibleItem<ItemSpinner.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {

        with(holder) {
            title.text = this@ItemSpinner.title
            val typesAdapter = TypesArrayAdapter(
                    itemView.context,
                    android.R.layout.simple_list_item_1,
                    typesList.toTypedArray())
            spinner.adapter = typesAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    typeListener.invoke(typesList[position])
                }
            }

            (spinner.adapter as? TypesArrayAdapter)?.getPosition(selectedType)
                    ?.let { spinner.setSelection(it) }
        }

    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemSpinner.ViewHolder(view, adapter)

    override fun equals(other: Any?): Boolean {
        other is ItemSpinner
        return other.toString() == this.toString()
    }

    override fun getLayoutRes() = R.layout.holder_spinner

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {

        val title: TextView = view.findViewById<TextView>(R.id.titleDifficult)
        val spinner: Spinner = view.findViewById<Spinner>(R.id.spinnerDifficult)
    }

    override fun toString(): String {
        return title
    }
}
