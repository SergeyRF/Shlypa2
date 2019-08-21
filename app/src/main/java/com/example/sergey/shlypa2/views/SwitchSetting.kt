package com.example.sergey.shlypa2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sergey.shlypa2.R
import kotlinx.android.synthetic.main.switch_setting.view.*

/**
 * Created by alex on 4/25/18.
 */
class SwitchSetting @JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {


    init {
        LayoutInflater.from(context).inflate(R.layout.switch_setting, this, true)

        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.SwitchSetting)

            tvTitleSwitch.text = array.getText(R.styleable.SwitchSetting_switch_setting_title)
        }
        setOnClickListener {
            setChecked(!isChecked())
        }
    }

    fun isChecked(): Boolean = switchSetting.isChecked

    fun setChecked(checked: Boolean): SwitchSetting {
        switchSetting.isChecked = checked
        return this
    }

    fun setOnCheckedListener(listener: (Boolean) -> Unit): SwitchSetting {
        switchSetting.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            listener.invoke(isChecked)
        })
        return this
    }

    fun setTitle(title: String): SwitchSetting {
        tvTitleSwitch.text = title
        return this
    }

}