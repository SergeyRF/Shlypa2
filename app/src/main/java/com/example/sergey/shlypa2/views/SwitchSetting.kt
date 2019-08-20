package com.example.sergey.shlypa2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sergey.shlypa2.R

/**
 * Created by alex on 4/25/18.
 */
class SwitchSetting @JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: TextView
    private var switch: Switch

    init {
        LayoutInflater.from(context).inflate(R.layout.switch_setting, this, true)

        title = findViewById(R.id.tvTitleSwitch)
        switch = findViewById(R.id.switchSetting)

        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.SwitchSetting)

            title.text = array.getText(R.styleable.SwitchSetting_switch_setting_title)
        }
        setOnClickListener {
            setChecked(!isChecked())
        }
    }

    fun isChecked(): Boolean = switch.isChecked

    fun setChecked(checked: Boolean): SwitchSetting {
        switch.isChecked = checked
        return this
    }

    fun setOnCheckedListener(listener: (Boolean) -> Unit): SwitchSetting {
        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            listener.invoke(isChecked)
        })
        return this
    }

    fun setTitle(title: String): SwitchSetting {
        this.title.text = title
        return this
    }

}