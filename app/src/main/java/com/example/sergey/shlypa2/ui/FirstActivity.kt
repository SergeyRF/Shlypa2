package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.crashlytics.android.Crashlytics
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ui.fragments.LoadStateFragment
import com.example.sergey.shlypa2.ui.fragments.WelcomeFragment
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.WelcomeViewModel
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : AppCompatActivity() {

    lateinit var viewModel : WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        supportActionBar?.elevation = 0f

        viewModel = ViewModelProviders.of(this).get(WelcomeViewModel::class.java)

        viewModel.commandsCallBack.observe(this, Observer { command ->
            when(command) {
                WelcomeViewModel.Commands.NEW_GAME -> startNewGame()
                WelcomeViewModel.Commands.RULES -> startRulesFragment()
                WelcomeViewModel.Commands.SAVED_GAMES -> startGameLoadFragment()
            }
        })
    }

    override fun onStart() {
        super.onStart()

        if(supportFragmentManager.findFragmentById(R.id.containerFirst) == null) {
            val fragment = WelcomeFragment()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.containerFirst, fragment)
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.themes_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(item.itemId) {
                R.id.item_blue -> Functions.selectTheme(R.style.AppThemeBlue, this)
                R.id.item_cyan -> Functions.selectTheme(R.style.AppThemeCyan, this)
                R.id.item_teal -> Functions.selectTheme(R.style.AppTheme, this)
                R.id.item_indigo -> Functions.selectTheme(R.style.AppThemeIndigo, this)
                R.id.item_purple -> Functions.selectTheme(R.style.AppThemePurple, this)
                R.id.item_green -> Functions.selectTheme(R.style.AppThemeGreen, this)
                R.id.item_yellow -> Functions.selectTheme(R.style.AppThemeYellow, this)
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
                .replace(R.id.containerFirst, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun startRulesFragment() {

    }
}
