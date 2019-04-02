package com.example.sergey.shlypa2.extensions

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

fun Context.runOnceEver(prefKey: String, block: () -> Unit) {
    val preference = PreferenceManager.getDefaultSharedPreferences(this)
    if (!preference.getBoolean(prefKey, false)) {
        block.invoke()
        preference.edit()
                .putBoolean(prefKey, true)
                .apply()
    }
}

inline fun <reified T: Any> Activity.extra(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    if (value is T) value else default
}

inline fun <reified T: Any> Activity.extraNotNull(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}

inline fun <reified T: Any> Fragment.extra(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else default
}
inline fun <reified T: Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}