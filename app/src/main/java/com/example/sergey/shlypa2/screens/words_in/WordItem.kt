package com.example.sergey.shlypa2.screens.words_in


import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.extensions.gone
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder


class WordItem(private val flagChange: Boolean,
               private val word: Word,
               private val listenerAction:(Pair<WordAct,Int>)->Unit) : AbstractFlexibleItem<WordItem.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
        with(holder){
            tvName.text = word.word
            if (!flagChange) ibChangeWord.gone()

            ibDeleteWord.setOnClickListener {
                listenerAction.invoke(Pair(WordAct.DELETE,position))
            }

            ibChangeWord.setOnClickListener {
                listenerAction.invoke(Pair(WordAct.CHANGE,position))
            }
        }

    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = WordItem.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is WordItem
            && other.word == word
            && other.flagChange == flagChange

    override fun getLayoutRes() = R.layout.holder_word

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {
        val tvName: TextView = view.findViewById(R.id.wordInject)
        val ibDeleteWord: ImageButton = view.findViewById(R.id.ibDelWord)
        val ibChangeWord: ImageButton = view.findViewById(R.id.ibChangeWord)
    }
}

enum class WordAct{
    DELETE,
    CHANGE
}