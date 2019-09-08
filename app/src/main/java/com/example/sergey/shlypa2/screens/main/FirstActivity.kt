package com.example.sergey.shlypa2.screens.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.example.sergey.shlypa2.AppRater
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ads.ConsentManager
import com.example.sergey.shlypa2.extensions.extraNotNull
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.selectTheme
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.main.pages.LoadStateFragment
import com.example.sergey.shlypa2.screens.main.pages.RulesFragment
import com.example.sergey.shlypa2.screens.main.pages.WelcomeFragment
import com.example.sergey.shlypa2.screens.players.PlayersActivity
import com.example.sergey.shlypa2.screens.premium.BuyPremiumActivity
import com.example.sergey.shlypa2.screens.settings.SettingsActivity
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.since
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class FirstActivity : AppCompatActivity(R.layout.activity_first) {

    companion object {
        private const val REQUEST_PREMIUM = 1020
        private const val EXTRA_SHOW_RATE = "extra_show_rate"
        fun getIntent(context: Context, showRate: Boolean = false) =
                Intent(context, FirstActivity::class.java)
                        .putExtra(EXTRA_SHOW_RATE, showRate)
    }

    private val viewModel by viewModel<WelcomeViewModel>()
    private val consentManager by inject<ConsentManager>()

    var themeId: Int = 0

    private val showRate by extraNotNull(EXTRA_SHOW_RATE, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)

        if (showRate) {
            AppRater().rateAppIfRequired(this)
        }

        supportActionBar?.elevation = 0f

        viewModel.commandsCallBack.observeSafe(this) { command ->
            when (command) {
                WelcomeViewModel.Commands.NEW_GAME -> startNewGame()
                WelcomeViewModel.Commands.RULES -> startRulesFragment()
                WelcomeViewModel.Commands.SAVED_GAMES -> startGameLoadFragment()
            }
        }
    }


    override fun onStart() {
        super.onStart()

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

        consentManager.showConsentIfNeed(this) {
            startActivityForResult(BuyPremiumActivity.getIntent(this), REQUEST_PREMIUM)
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
                R.id.item_share -> share()
                android.R.id.home -> {
                    onBackPressed()
                }
            }
        }

        return true
    }

    private fun share() {
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain") // or "message/rfc822"
                .setChooserTitle(R.string.invite_friend)
                .setText(Constants.APP_STORE_LINK)
                .startChooser()
    }

    private fun startNewGame() {
        startActivity(PlayersActivity.getIntent(this))
    }

    private fun startGameLoadFragment() {
        val fragment = LoadStateFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        initToolbar()
    }

    private fun startRulesFragment() {
        val fragment = RulesFragment()
//        val fragment = MotionTestFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        initToolbar()
    }

    private fun initToolbar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun disableToolbar() {
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        disableToolbar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_PREMIUM -> {
                consentManager.setPremium(resultCode == Activity.RESULT_OK)
                consentManager.showConsentIfNeed(this)
            }
        }
    }
}
