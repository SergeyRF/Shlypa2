package com.example.sergey.shlypa2.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.RvImageAdapter

/**
 * Created by alex on 4/21/18.
 */

class AvatarSelectDialog(val context: Context, val images: List<String>) {

    var onSelect: ((String) -> Unit)? = null
    var onSelectCustom: ((CustomAvatar) -> Unit)? = null

    fun show() {
        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_avatar_select)

        val loadImage: CardView = dialog.findViewById(R.id.cvAddUserAvatar)

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

        loadImage.setOnClickListener {
            onSelectCustom?.invoke(CustomAvatar.IMAGE)
            dialog.dismiss()
        }

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

enum class CustomAvatar {
    IMAGE,
    PHOTO
}