package com.example.sergey.shlypa2.screens.players.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.Functions
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class ItemIcon(val iconString: String) : AbstractFlexibleItem<ItemIcon.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>?) {

        with(holder) {
            Glide.with(itemView)
                    .load(Functions.imageNameToUrl("player_avatars/small/$iconString"))
                    .into(iv)
        }

    }

    override fun hashCode(): Int {
        return iconString.hashCode()
    }

    override fun equals(other: Any?): Boolean = other is ItemIcon

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun getLayoutRes(): Int {
        return R.layout.holder_image
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {
        val iv: ImageView = view.findViewById(R.id.ivImageDialog)

    }
}