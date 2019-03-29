package com.example.sergey.shlypa2.extensions

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.ViewGroup
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.PreferenceHelper
import com.example.sergey.shlypa2.utils.PreferenceHelper.get
import com.example.sergey.shlypa2.utils.PreferenceHelper.set


fun Activity.selectTheme(theme: Int) {
    val preferences = PreferenceHelper.defaultPrefs(this)
    val oldTheme = preferences[Constants.THEME_PREF] ?: com.example.sergey.shlypa2.R.style.AppTheme

    if(theme != oldTheme) {
        preferences[Constants.THEME_PREF] = theme
        recreate()
    }
}

fun Activity.setThemeApi21() {
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

    val preferences = PreferenceHelper.defaultPrefs(this)
    val themeRes: Int = preferences[Constants.THEME_PREF] ?: com.example.sergey.shlypa2.R.style.AppTheme

    setTheme(themeRes)
}
