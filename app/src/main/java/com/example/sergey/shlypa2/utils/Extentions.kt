package com.example.sergey.shlypa2.utils

import android.content.Context
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import java.util.*

/**
 * Created by alex on 4/10/18.
 */

fun SeekBar.onChange( listener : (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit) {
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            listener.invoke(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    })
}

fun View.hide(){
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


/**
 * Returns a random element.
 */
fun <E> List<E>.random(): E? = if (size > 0) get(Random().nextInt(size)) else null