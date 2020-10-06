package com.example.sergey.shlypa2.utils

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
         FirebaseCrashlytics.getInstance().setCustomKey("Timber_log_error",message)
            t?.let {
                FirebaseCrashlytics.getInstance()
                        .setCustomKey("Timber_throwable",it.message?:it.stackTrace.contentToString())
            }
        }
    }
}