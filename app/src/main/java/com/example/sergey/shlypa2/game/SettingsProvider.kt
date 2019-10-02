package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Settings

/**
 * Created by alex on 4/10/18.
 */
interface SettingsProvider {
    fun getSettings() : Settings
    fun writeSettings(settings: Settings)
}