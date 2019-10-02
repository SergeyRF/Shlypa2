package com.example.sergey.shlypa2.screens.players.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.*
import kotlinx.android.synthetic.main.dialog_edit_text.*

class RenameDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_NAME = "arg_name"
        private const val ARG_TITLE = "arg_title"
        private const val ARG_ID = "arg_id"
        private const val ARG_TYPE = "arg_type"

        fun newInstance(name: String, title: String,
                        id: Long, type: EntityType): RenameDialogFragment {
            return RenameDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_TITLE, title)
                    putLong(ARG_ID, id)
                    putInt(ARG_TYPE, type.ordinal)
                }
            }
        }
    }

    private val name by extraNotNull<String>(ARG_NAME)
    private val title by extraNotNull<String>(ARG_TITLE)
    private val entityId by extraNotNull<Long>(ARG_ID)
    private val type by lazy {
        val ordinal = requireArguments()
                .getInt(ARG_TYPE)
        EntityType.values()[ordinal]
    }

    private var listener: RenameDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.RenameDialogStyle)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        dialog.setContentView(R.layout.dialog_edit_text)
        initViews()
    }

    private fun initViews() {
        dialog?.let { dialog ->
            dialog.tvReNameIt.text = title

            with(dialog.etDialog) {
                onDrawn { showKeyboard() }
                setText(name)
                setSelection(name.length)
                onActionDone { onNameAccepted() }
            }

            dialog.btYesDialog.setOnClickListener {
                onNameAccepted()
            }

            dialog.btNoDialog.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun onNameAccepted() {
        dialog?.let {
            val newName = it.etDialog.text.toString()
            if (newName != name) {
                listener?.onRenamed(newName, name, entityId, type)
            }
            dismiss()
        }
    }

    override fun dismiss() {
        dialog?.etDialog?.hideKeyboard()
        super.dismiss()
    }

    override fun onCancel(dialogInterface: DialogInterface) {
        dialog?.etDialog?.hideKeyboard()
        super.onCancel(dialogInterface)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as RenameDialogListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    enum class EntityType {
        PLAYER,
        TEAM
    }

    interface RenameDialogListener {
        fun onRenamed(newName: String, oldName: String, id: Long, type: EntityType)
    }
}