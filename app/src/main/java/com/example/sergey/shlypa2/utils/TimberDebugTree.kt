package com.example.sergey.shlypa2.utils

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber

/**
 * Created by alex on 4/29/18.
 */

class TimberDebugTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
    }

    override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        super.log(priority, t, message, *args)
    }


}

class TimberReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if(priority == Log.ERROR) {
            Crashlytics.log(message)
            t?.let {
                Crashlytics.logException(it)
            }
        }
    }
}