package com.example.sergey.shlypa2.data

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class AppLifecycleObserver(
        private val configProvider: ConfigsProvider
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        configProvider.updateConfigs()
    }
}