package com.example.sergey.shlypa2.screens.players.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.extraNotNull
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
    private val entytityId by extraNotNull<Long>(ARG_ID)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(
            R.layout.dialog_edit_text, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(true)

        tvReNameIt.text = title
        etDialog.hint = name

        btYesDialog.setOnClickListener {
            if (etDialog.text.isNotEmpty()) {
                val newName = etDialog.text.toString()
                listener?.onRenamed(newName, name, entytityId, type)
            }
            dismissAllowingStateLoss()
        }

        btNoDialog.setOnClickListener {
            dismissAllowingStateLoss()
        }
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