package com.example.sergey.shlypa2.screens.players.adapter


import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.extensions.hide
import com.example.sergey.shlypa2.extensions.show
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import timber.log.Timber


class ItemPlayer(val player: Player,
                 val renameListener: (Player) -> Unit = {},
                 val removeListener: (Player) -> Unit = {}) : AbstractFlexibleItem<ItemPlayer.ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {
      with(holder) {
          tvName.show()
          etName.hide()
          delPlayer.hide()
          tvName.text = player.name

          itemView.setOnClickListener {
              tvName.hide()
              etName.show()
              delPlayer.show()
              etName.setText("")
              etName.append(player.name)
              etName.requestFocus()
              Functions.showKeyboard(itemView.context, etName)
          }

          delPlayer.setOnClickListener {
              removeListener.invoke(player)
          }

          etName.setOnFocusChangeListener { view, hasFocus ->
              Timber.d(" focus changed$hasFocus")
              if (!hasFocus) {
                  if (etName.text.isNotEmpty() && etName.text.toString() != player.name) {
                      player.name = etName.text.toString()
                      tvName.text = player.name
                      renameListener.invoke(player)
                  }

                  tvName.show()
                  etName.hide()
                  delPlayer.hide()
              }
          }

          Picasso.get()
                  .load(Functions.imageNameToUrl("player_avatars/small/${player.avatar}"))
                  .into(avatarImage)
      }
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) = ItemPlayer.ViewHolder(view, adapter)

    override fun equals(other: Any?) = other is ItemPlayer

    override fun getLayoutRes() = R.layout.holder_player

    class ViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?)
        : FlexibleViewHolder(view, adapter) {
        val tvName: TextView = itemView.findViewById(R.id.wordInject)
        val etName: EditText = itemView.findViewById(R.id.etRename)
        val avatarImage: CircleImageView = itemView.findViewById(R.id.civPlayerAvatar)
        val delPlayer: ImageButton = itemView.findViewById(R.id.ib_delPlayer)
    }
}