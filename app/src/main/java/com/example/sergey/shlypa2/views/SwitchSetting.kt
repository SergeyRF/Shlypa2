package com.example.sergey.shlypa2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.example.sergey.shlypa2.R

/**
 * Created by alex on 4/25/18.
 */
class SwitchSetting : LinearLayout {

    private var title: TextView
    private var switch: Switch

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.switch_setting, this, true)

        title = findViewById(R.id.tvTitleSetting)
        switch = findViewById(R.id.switchSetting)

        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.SwitchSetting)

            title.setText(array.getText(R.styleable.SwitchSetting_switch_setting_title))
        }
    }

    fun isChecked(): Boolean = switch.isChecked

    fun setChecked(checked: Boolean) = switch.setChecked(checked)

    fun setOnCheckedListener(listner : CompoundButton.OnCheckedChangeListener?)
            = switch.setOnCheckedChangeListener(listner)

}