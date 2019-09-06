package com.example.sergey.shlypa2.data

import com.example.sergey.shlypa2.BuildConfig
import com.example.sergey.shlypa2.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class ConfigsProvider(private val firebaseRemoteConfig: FirebaseRemoteConfig) {

    companion object {
        private const val INTERSTITIAL_ENABLED_KEY = "interstitial_enabled"
        private const val INTERSTITIAL_DELAY_KEY = "interstitial_delay"
        private const val NATIVE_BEFORE_TURN_ENABLED_KEY = "native_before_turn_enabled"
        private const val FETCH_INTERVAL = 3600L * 12 //12 hours
    }

    private var initialized = false

    var interstitialEnabled: Boolean = true
        private set
    var interstitialDelaySec: Long = 10 * 60
        private set
    var nativeBeforeTurnEnabled: Boolean = true
        private set

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(if(BuildConfig.DEBUG) 10 else FETCH_INTERVAL)
                .build()

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
                .addOnSuccessListener {
                    firebaseRemoteConfig.setDefaults(R.xml.remote_configs_defaults)
                    initialized = true
                    updateConfigs(true)
                }
    }

    fun updateConfigs() {
        if (initialized) {
            updateConfigs(true)
        }
    }

    private fun updateConfigs(forceApply: Boolean) {
        firebaseRemoteConfig.fetchAndActivate()
                .addOnSuccessListener {
                    applyConfigs()
                }
    }

    private fun applyConfigs() {
        with(firebaseRemoteConfig) {
            interstitialEnabled = getBoolean(INTERSTITIAL_ENABLED_KEY)
            interstitialDelaySec = getLong(INTERSTITIAL_DELAY_KEY)
            nativeBeforeTurnEnabled = getBoolean(NATIVE_BEFORE_TURN_ENABLED_KEY)
        }

    }
}