package com.example.sergey.shlypa2.screens.game

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ads.AdsManager
import com.example.sergey.shlypa2.ads.Interstitial
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.game_result.GameResultActivity
import com.google.android.gms.ads.AdListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

typealias Command = RoundViewModel.Command

class RoundActivity : AppCompatActivity() {

    private val viewModel: RoundViewModel by viewModel()
    private val adsManager: AdsManager by inject()
    private var interstitial: Interstitial? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round)

        initToolbar()

        supportActionBar?.elevation = 0F

        if (adsManager.initialized) {
            interstitial = adsManager.getInterstitial(this)
            interstitial?.loadAd()
            interstitial?.setAdListener(object : AdListener() {
                override fun onAdClosed() {
                    interstitial?.loadAd()
                }
            })
        }

        viewModel.commandCallback.observeSafe(this) { command ->
            when (command) {
                Command.GET_READY -> startGetReadyFragment()
                Command.START_TURN -> startGameFragment()
                Command.FINISH_TURN -> startTurnResultFragment()
                Command.SHOW_ROUND_RESULTS -> startRoundResultsFragment()
                Command.START_NEXT_ROUND -> startNextRound()
                Command.SHOW_GAME_RESULTS -> startGameResults()
                Command.SHOW_HINT_TEAM_TABLE -> startHintTeam()
                Command.SHOW_INTERSTITIAL_ADS -> showInterstitial()
                Command.EXIT -> finish()
            }
        }

        viewModel.roundLiveData.observeSafe(this){
            it?.let { setTitle(it.description) }
        }

        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            startStartFragment()
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is TeamHintFragment) {
            super.onBackPressed()
        } else {
            leaveGameDialog()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        viewModel.saveState()
    }

    private fun startHintTeam() = startFragment(TeamHintFragment(), true)

    private fun startStartFragment() = startFragment(RoundStartFragment(), true)

    private fun startGetReadyFragment() = startFragment(TurnStartFragment())

    private fun startGameFragment() = startFragment(GameFragment())

    private fun startTurnResultFragment() = startFragment(TurnResultFragment())

    private fun startRoundResultsFragment() = startFragment(RoundResultFragment())

    private fun startNextRound() {
        startActivity(Intent(this, RoundActivity::class.java))
        finish()
    }

    private fun startGameResults() {
        startActivity(Intent(this, GameResultActivity::class.java))
        finish()
    }

    private fun startFragment(fragment: androidx.fragment.app.Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
                .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)

        if (addToBackStack) transaction.addToBackStack(null)

        transaction.commit()
    }

    private fun showInterstitial() {
        interstitial?.showAd()
    }

    private fun leaveGameDialog() {

        AlertDialog.Builder(this).apply {
            setTitle(R.string.leaveTitle)
            setMessage(R.string.leaveMessage)
            setPositiveButton(R.string.leavePositive) { _, _ ->
                viewModel.onFinishGameAccepted()
            }
            setNegativeButton(R.string.leaveNegative) { dialog, _ -> dialog.cancel() }
        }
                .create()
                .show()
    }

    private fun initToolbar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
