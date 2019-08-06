package com.example.sergey.shlypa2.views

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.TextView
import com.example.sergey.shlypa2.R

/**
 * Created by alex on 4/25/18.
 */
class SeekbarSetting : ConstraintLayout {

    lateinit var title : TextView
    lateinit var count : TextView
    lateinit var seekbar : SeekBar

    var seekbarListener : ((seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit)? = null

    private var minValue = 0
    private var maxValue = 0


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
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

    fun setValues(min : Int, max : Int):SeekbarSetting {
        minValue = min
        maxValue = max
        seekbar.max = maxValue - minValue
        return this
    }

    fun setProgress(progress : Int):SeekbarSetting {
        seekbar.progress = progress - minValue
        return this
    }

    fun setProgressListener(listener:(Int)->Unit):SeekbarSetting{
        seekbarListener ={_,progress,_->
            listener.invoke(progress)
        }
        return this
    }

    val listener  = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            count.text = (progress + minValue).toString()
            if(fromUser) seekbarListener?.invoke(seekBar, progress + minValue, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }
}