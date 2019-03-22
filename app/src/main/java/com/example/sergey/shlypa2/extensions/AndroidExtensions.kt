package com.example.sergey.shlypa2.extensions

import android.content.Context
import androidx.preference.PreferenceManager

fun Context.runOnceEver(prefKey: String, block: () -> Unit) {
    val preference = PreferenceManager.getDefaultSharedPreferences(this)
    if(!preference.getBoolean(prefKey, false)) {
        block.invoke()
        preference.edit()
                .putBoolean(prefKey, true)
                .apply()
    }
}