package com.example.sergey.shlypa2.screens.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        initToolbar()

        if (supportFragmentManager.findFragmentById(android.R.id.content) == null) {
            val fragment = SettingsFragment()
            supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit()
        }
    }

    fun showContentLicenses() {
        val fragment = ContentLicensesFragment()
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun showDeletePlayers() {
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, DeletePlayerFragment())
                .addToBackStack(null)
                .commit()
    }

    fun showOpensourceLicenses() {
        LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withFields(R.string::class.java.fields)
                .start(this)
    }

    private fun initToolbar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
