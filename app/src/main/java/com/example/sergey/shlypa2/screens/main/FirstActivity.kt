package com.example.sergey.shlypa2.screens.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.sergey.shlypa2.AppRater
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.selectTheme
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.extensions.setThemedBackground
import com.example.sergey.shlypa2.screens.players.PlayersActivity
import com.example.sergey.shlypa2.ui.fragments.LoadStateFragment
import com.example.sergey.shlypa2.ui.fragments.RulesFragment
import com.example.sergey.shlypa2.ui.fragments.WelcomeFragment
import com.example.sergey.shlypa2.ui.settings.SettingsActivity
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.since
import com.example.sergey.shlypa2.viewModel.WelcomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FirstActivity : AppCompatActivity() {

    private val viewModel by viewModel<WelcomeViewModel>()

    var themeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        setThemedBackground()

        supportActionBar?.elevation = 0f

        viewModel.commandsCallBack.observe(this, Observer { command ->
            when (command) {
                WelcomeViewModel.Commands.NEW_GAME -> startNewGame()
                WelcomeViewModel.Commands.RULES -> startRulesFragment()
                WelcomeViewModel.Commands.SAVED_GAMES -> startGameLoadFragment()
            }
        })
    }


    override fun onStart() {
        super.onStart()
        val r = AppRater()
        r.app_launched(this)

        since(Build.VERSION_CODES.LOLLIPOP) {
            if (Functions.getSelectedThemeId(this) != themeId) {
                recreate()
            }
        }

        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            val fragment = WelcomeFragment()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()
        }

    }

    override fun setTheme(resid: Int) {
        super.setTheme(resid)
        themeId = resid
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                R.id.item_blue -> selectTheme(R.style.AppThemeBlue)
                R.id.item_cyan -> selectTheme(R.style.AppThemeCyan)
                R.id.item_teal -> selectTheme(R.style.AppTheme)
                R.id.item_indigo -> selectTheme(R.style.AppThemeIndigo)
                R.id.item_purple -> selectTheme(R.style.AppThemePurple)
                R.id.item_green -> selectTheme(R.style.AppThemeGreen)
                R.id.item_yellow -> selectTheme(R.style.AppThemeYellow)

                R.id.item_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        return true
    }

    private fun startNewGame() {
        val intent = Intent(this, PlayersActivity::class.java)
        startActivity(intent)
    }

    private fun startGameLoadFragment() {
        val fragment = LoadStateFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun startRulesFragment() {
        val fragment = RulesFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()

    }


}
