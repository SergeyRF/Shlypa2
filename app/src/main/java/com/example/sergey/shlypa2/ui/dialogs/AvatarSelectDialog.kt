package com.example.sergey.shlypa2.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.view.WindowManager
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.RvImageAdapter

/**
 * Created by alex on 4/21/18.
 */

class AvatarSelectDialog(val context: Context, val images: List<String>) {

    var onSelect: ((String) -> Unit)? = null

    fun show() {
        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_avatar_select)

        val rv: RecyclerView = dialog.findViewById(R.id.rvAvatarDialog)
        val gridLayout = GridLayoutManager(context, 4)
        rv.layoutManager = gridLayout

        val adapter = RvImageAdapter()
        adapter.setData(images)
        adapter.listener = { file ->
            dialog.dismiss()
            onSelect?.invoke(file)
        }

        rv.adapter = adapter

        dialog.show()

        //Set sizes for dialog
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window.attributes)

        val width = Functions.getScreenWidth(context)
        layoutParams.width = width
        layoutParams.height = (width * 0.7).toInt()

        dialog.window.attributes = layoutParams
    }
}