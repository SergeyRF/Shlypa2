package com.example.sergey.shlypa2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sergey.shlypa2.R
import kotlinx.android.synthetic.main.setting_seekbar.view.*

/**
 * Created by alex on 4/25/18.
 */
class SeekbarSetting @JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {


    private var seekbarListener: ((seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit)? = null

    private var minValue = 0
    private var maxValue = 0

    private val listener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            tvCountSetting.text = (progress + minValue).toString()
            if (fromUser) seekbarListener?.invoke(seekBar, progress + minValue, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.setting_seekbar, this, true)

        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.SeekbarSetting)
            tvTitleSwitch.text = array.getText(R.styleable.SeekbarSetting_seek_setting_title)
            array.recycle()
        }

        sbSetting.setOnSeekBarChangeListener(listener)
    }

    fun setValues(min: Int, max: Int): SeekbarSetting {
        minValue = min
        maxValue = max
        sbSetting.max = maxValue - minValue
        return this
    }

    fun setProgress(progress: Int): SeekbarSetting {
        sbSetting.progress = progress - minValue
        return this
    }

    fun setProgressListener(listener: (Int) -> Unit): SeekbarSetting {
        seekbarListener = { _, progress, _ ->
            listener.invoke(progress)
        }
        return this
    }

    fun setTitle(title: String): SeekbarSetting {
        tvTitleSwitch.text = title
        return this
    }


}