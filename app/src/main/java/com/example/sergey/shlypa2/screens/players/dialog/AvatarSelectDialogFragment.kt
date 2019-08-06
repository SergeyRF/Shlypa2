package com.example.sergey.shlypa2.screens.players.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.extraNotNull
import com.example.sergey.shlypa2.screens.players.adapter.ItemIcon
import com.example.sergey.shlypa2.utils.Functions
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.dialog_avatar_select.*

class AvatarSelectDialogFragment(private val listener: AvatarSelectDialogListener)
    : DialogFragment(),
        FlexibleAdapter.OnItemClickListener {

    companion object {
        private const val LIST_ICON = "list_icon"

        fun newInstance(listIcon: List<String>, setListener: AvatarSelectDialogListener): AvatarSelectDialogFragment {
            return AvatarSelectDialogFragment(setListener).apply {
                arguments = Bundle().apply {
                    putStringArrayList(LIST_ICON, ArrayList(listIcon))
                }
            }
        }
    }

    private val iconList by extraNotNull<ArrayList<String>>(LIST_ICON)


    var iconAdapter = FlexibleAdapter(emptyList(), this)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dialog_avatar_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(true)

        with(rvAvatarDialog) {
            layoutManager = GridLayoutManager(context, 4)
            adapter = iconAdapter
        }

        iconAdapter.updateDataSet(
                iconList.map {
                    ItemIcon(it)
                }
        )
        btAddUserImage.setOnClickListener {
            listener.onSelectCustomAvatar()
            dismissAllowingStateLoss()
        }

    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        return when (val item = iconAdapter.getItem(position)) {
            is ItemIcon -> {
                listener.onSelectAvatar(item.iconString)
                dismissAllowingStateLoss()
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        setDialogSize()
    }

    private fun setDialogSize() {
        val width = Functions.getScreenWidth(dialog!!.context)
        val height = (width * 1.2).toInt()
        dialog?.window?.setLayout(width, height)
    }

}