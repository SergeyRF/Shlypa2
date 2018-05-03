package com.example.sergey.shlypa2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.GameState
import com.example.sergey.shlypa2.game.TeamWithScores


class RvAdapter : RecyclerView.Adapter<BaseHolder>() {

    private var data: List<Any>? = null

    var listener:((Any) -> Unit)? = null
    var listenerTwo:((Any)->Unit)? = null
    var listenerThree:((Any)->Unit)? = null

    var altMode = false


    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val item = data!![position]
        when (holder) {
            is PlayerHolder -> holder.bind(item as Player)
            is WordsHolder -> holder.bind(item as Word)
            is WordResultHolder -> holder.bind(item as Word)
            is TeamHolder -> holder.bind(item as Team)
            is TeamWithScoreHolder -> holder.bind(item as TeamWithScores)
            is SavedStateHolder -> holder.bind(item as GameState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val holder = when (viewType) {
            VIEW_TYPE_PLAYER -> PlayerHolder(view)
            VIEW_TYPE_WORD -> WordsHolder(view)
            VIEW_TYPE_WORD_RESULT -> WordResultHolder(view)
            VIEW_TYPE_TEAM -> TeamHolder(view)
            VIEW_TYPE_TEAM_SCORES -> TeamWithScoreHolder(view)
            VIEW_TYPE_SAVED_STATE -> SavedStateHolder(view)
            else -> throw RuntimeException("Unsupported item type")
        }

        holder.listener = listener
        holder.listenerTwo= listenerTwo
        holder.listenerThree = listenerThree
        return holder
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        val item = data!![position]
        return when (item) {
            is Player -> VIEW_TYPE_PLAYER
            is Word -> if(altMode) VIEW_TYPE_WORD_RESULT else VIEW_TYPE_WORD
            is Team -> VIEW_TYPE_TEAM
            is TeamWithScores -> VIEW_TYPE_TEAM_SCORES
            is GameState -> VIEW_TYPE_SAVED_STATE
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
        const val VIEW_TYPE_WORD_RESULT = R.layout.holder_word_resalt
        const val VIEW_TYPE_TEAM = R.layout.holder_teem
        const val VIEW_TYPE_TEAM_SCORES = R.layout.holder_team_score
        const val VIEW_TYPE_SAVED_STATE = R.layout.holder_saved_state
    }
}

