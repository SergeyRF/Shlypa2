package com.example.sergey.shlypa2.data

import com.example.sergey.shlypa2.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class RemoteConfigsProvider(private val firebaseRemoteConfig: FirebaseRemoteConfig) {

    companion object {
        private const val INTERSTITIAL_ENABLED_KEY = "interstitial_enabled"
        private const val INTERSTITIAL_DELAY_KEY = "interstitial_delay"
        private const val FETCH_INTERVAL = 3600L * 12 //12 hours
    }

    private var initialized = false

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(FETCH_INTERVAL)
                .build()

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
                .addOnSuccessListener {
                    firebaseRemoteConfig.setDefaults(R.xml.remote_configs_defaults)
                    initialized = true
                    updateConfigs()
                }
    }

    private fun updateConfigs() {

    }
}