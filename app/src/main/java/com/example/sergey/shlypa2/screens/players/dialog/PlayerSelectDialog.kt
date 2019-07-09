package com.example.sergey.shlypa2.screens.players.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.screens.players.adapter.RvAdapterPlayerSelect
import com.example.sergey.shlypa2.utils.Functions

class PlayerSelectDialog(val context: Context, val players: List<Player>) {

    var onSelect: ((Player) -> Unit)? = null


    fun show() {
        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_avatar_select)

        val bt = dialog.findViewById<Button>(R.id.btAddUserImage)
        bt.visibility = View.GONE

        val rv: RecyclerView = dialog.findViewById(R.id.rvAvatarDialog)
        val gridLayout = GridLayoutManager(context, 4)
        rv.layoutManager = gridLayout

        val adapter = RvAdapterPlayerSelect()
        adapter.setData(players)
        adapter.listener = { player ->
            dialog.dismiss()
            onSelect?.invoke(player)
        }

        rv.adapter = adapter

        dialog.show()

        //Set sizes for dialog
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window.attributes)

        val width = Functions.getScreenWidth(context)
        layoutParams.width = width
        layoutParams.height = (width * 1.2).toInt()

        dialog.window.attributes = layoutParams
    }
}