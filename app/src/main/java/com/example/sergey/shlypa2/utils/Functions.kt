package com.example.sergey.shlypa2.utils

import android.content.Context
import java.text.DateFormat
import java.util.*
import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import com.example.sergey.shlypa2.utils.PreferenceHelper.get
import timber.log.Timber

/**
 * Created by alex on 4/17/18.
 */
object Functions {

    fun timeToLocalDate(time: Long, context: Context): String {
        val date = Date(time)
        val dateFormat: DateFormat = android.text.format.DateFormat.getDateFormat(context)
        return dateFormat.format(date)
    }

    fun getGameId(context: Context) : Int {
        var id = PreferenceHelper.defaultPrefs(context)["Game id", 0] ?: 0
        PreferenceHelper.defaultPrefs(context)["Game id"] = ++id

        Timber.d("Game id is $id")
        return  id
    }

}