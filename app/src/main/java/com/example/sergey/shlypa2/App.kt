package com.example.sergey.shlypa2

import android.app.Application
import timber.log.Timber

/**
 * Created by alex on 4/4/18.
 */
class App  : Application(){

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}