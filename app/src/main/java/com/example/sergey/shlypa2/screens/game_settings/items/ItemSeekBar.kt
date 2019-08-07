package com.example.sergey.shlypa2.screens.game_settings.items


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.views.SeekbarSetting
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder


class ItemSeekBar(
        private val title: String,
        private val min: Int,
        private val max: Int,
        private val progress: Int,
        private val progressListener: (Int) -> Unit
) : AbstractFlexibleItem<ItemSeekBar.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
        with(holder) {
            seekBar.setTitle(title)
                    .setValues(min, max)
                    .setProgress(progress)
                    .setProgressListener {
                        progressListener.invoke(it)
                    }
        }

    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemSeekBar

    override fun getLayoutRes() = R.layout.holder_seekbar

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {

        val seekBar: SeekbarSetting = view.findViewById<SeekbarSetting>(R.id.itemSeekBar)

    }
}