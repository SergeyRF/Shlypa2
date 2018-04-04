package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergey.shlypa2.game.Player
import com.example.sergey.shlypa2.game.Team
import com.example.sergey.shlypa2.game.Word


class RvAdapter : RecyclerView.Adapter<BaseHolder>() {

    private var data: List<Any>? = null

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val item = data!![position]
        when (holder) {
            is PlayerHolder -> holder.bind(item as Player)
            is WordsHolder -> holder.bind(item as Word)
            is TeamHolder -> holder.bind(item as Team)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(viewType, parent, false)
        return when (viewType) {
            VIEW_TYPE_PLAYER -> PlayerHolder(view)
            VIEW_TYPE_WORD -> WordsHolder(view)
            VIEW_TYPE_TEAM -> TeamHolder(view)
            else -> throw RuntimeException("Unsupported item type")
        }
    }

    override fun getItemCount(): Int {
        return if (data == null) 0 else data!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = data!![position]
        return when (item) {
            is Player -> VIEW_TYPE_PLAYER
            is Word -> VIEW_TYPE_WORD
            is Team -> VIEW_TYPE_TEAM
            else -> throw RuntimeException("Unsupported item type")
        }
    }

    fun setData(list: List<Any>?) {
        data = list
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_PLAYER = R.layout.holder_player
        const val VIEW_TYPE_WORD = R.layout.holder_word
        const val VIEW_TYPE_TEAM = R.layout.holder_teem
    }
}

