package com.example.sergey.shlypa2.game

import android.content.Context
import com.example.sergey.shlypa2.utils.PreferenceHelper

import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import com.example.sergey.shlypa2.utils.PreferenceHelper.get
import com.google.gson.GsonBuilder
import timber.log.Timber


/**
 * Created by alex on 4/10/18.
 */
class SettingsProviderImpl(context: Context) : SettingsProvider {

    val preferences = PreferenceHelper.defaultPrefs(context)
    val  gson = GsonBuilder().create()

    override fun getSettings(): Settings {
        var settingsJson = preferences[SAVED_SETTINGS, "{}"]
        Timber.d(settingsJson)
        var settings : Settings = gson.fromJson(settingsJson, Settings::class.java)

        return settings
    }

    override fun writeSettings(settings: Settings) {
       var settingsJson : String = gson.toJson(settings)
        Timber.d(settingsJson)
        preferences[SAVED_SETTINGS] = settingsJson
    }

    companion object {
        const val SAVED_SETTINGS = "saved_settings"
    }
}