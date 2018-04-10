package com.example.sergey.shlypa2.game

/**
 * Created by alex on 4/10/18.
 */
interface SettingsProvider {
    fun getSettings() : Settings
    fun writeSettings(settings: Settings)
}