package com.example.sergey.shlypa2.utils

import android.content.Context
import java.text.DateFormat
import java.util.*

/**
 * Created by alex on 4/17/18.
 */
object Functions {

    fun timeToLocalDate(time: Long, context: Context): String {
        val date = Date(time)
        val dateFormat: DateFormat = android.text.format.DateFormat.getDateFormat(context)
        return dateFormat.format(date)
    }

}