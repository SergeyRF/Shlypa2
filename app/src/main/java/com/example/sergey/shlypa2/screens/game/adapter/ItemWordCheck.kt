package com.example.sergey.shlypa2.screens.game.adapter


import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.holder_word_resalt.view.*
import timber.log.Timber


class ItemWordCheck(val word: Word,
                    var checked: Boolean) : AbstractFlexibleItem<ItemWordCheck.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
      with(holder) {
          wordRv.text = word.word
          //avoid bug
          radioGroup.setOnCheckedChangeListener(null)
          radioGroup.check(if (checked) R.id.radioCorrect else R.id.radioWrong)

          radioGroup.setOnCheckedChangeListener { group, checkedId ->
              Timber.d("radio button checked")
              checked = checkedId == R.id.radioCorrect
          }
      }
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemWordCheck.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemWordCheck
            && other.word == word

    override fun getLayoutRes() = R.layout.holder_word_resalt

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {
        val wordRv: TextView = itemView.word_out
        val radioGroup: RadioGroup = itemView.groupCorrect
    }
}