package com.example.sergey.shlypa2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sergey.shlypa2.R

/**
 * Created by alex on 4/25/18.
 */
class SeekbarSetting @JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: TextView
    private lateinit var count: TextView
    private var seekbar: SeekBar

   private var seekbarListener: ((seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit)? = null

    private var minValue = 0
    private var maxValue = 0

    private val listener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            count.text = (progress + minValue).toString()
            if (fromUser) seekbarListener?.invoke(seekBar, progress + minValue, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.setting_seekbar, this, true)

        title = findViewById(R.id.tvTitleSetting)
        count = findViewById(R.id.tvCountSetting)
        seekbar = findViewById(R.id.sbSetting)

        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.SeekbarSetting)
            title.text = array.getText(R.styleable.SeekbarSetting_seek_setting_title)
            array.recycle()
        }

        seekbar.setOnSeekBarChangeListener(listener)
    }

    fun setValues(min: Int, max: Int): SeekbarSetting {
        minValue = min
        maxValue = max
        seekbar.max = maxValue - minValue
        return this
    }

    fun setProgress(progress: Int): SeekbarSetting {
        seekbar.progress = progress - minValue
        return this
    }

    fun setProgressListener(listener: (Int) -> Unit): SeekbarSetting {
        seekbarListener = { _, progress, _ ->
            listener.invoke(progress)
        }
        return this
    }

    fun setTitle(title:String):SeekbarSetting{
        this.title.text = title
        return this
    }


}