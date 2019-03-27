package com.example.sergey.shlypa2.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ads.AdsManager
import com.example.sergey.shlypa2.ads.Interstitial
import com.example.sergey.shlypa2.ui.fragments.*
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import com.google.android.gms.ads.AdListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

typealias Command = RoundViewModel.Command

class RoundActivity : AppCompatActivity() {

    private val viewModel by viewModel<RoundViewModel>()


    private var backPressedOnce = false
    private var interstitial: Interstitial? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setThemeApi21(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round)

        supportActionBar?.elevation = 0F

        if (AdsManager.initialized) {
            interstitial = AdsManager.getInterstitial(this)
            interstitial?.loadAd()
            interstitial?.setAdListener(object : AdListener() {
                override fun onAdClosed() {
                    interstitial?.loadAd()
                }
            })
        }

        viewModel.commandCallback.observe(this, Observer { command ->
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
        })

        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            startStartFragment()
        }

        viewModel.roundLiveData.observe(this, Observer {
            it?.let { setTitle(it.description) }
        })
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is TeamHintFragment) {
            super.onBackPressed()
        } else {
            if (backPressedOnce) {
                doAsync {
                    viewModel.portionClear(viewModel.saveGameState().get())
                    uiThread {
                        finish()
                    }
                }
            } else {
                backPressedOnce = true
                Toast.makeText(this, R.string.press_more_to_finish_game, Toast.LENGTH_LONG).show()
                Handler().postDelayed({ backPressedOnce = false }, 2000)
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        viewModel.saveGameState()
    }


    private fun startHintTeam() {
        val fragment = TeamHintFragment()
        startFragment(fragment, true)
    }

    private fun startStartFragment() {
        val fragment = RoundStartFragment()
        startFragment(fragment, true)
    }

    private fun startGetReadyFragment() {
        val fragment = TurnStartFragment()
        startFragment(fragment)
    }

    private fun startGameFragment() {
        val fragment = GameFragment()
        startFragment(fragment)
    }

    private fun startTurnResultFragment() {
        val fragment = TurnResultFragment()
        startFragment(fragment)
    }

    private fun startRoundResultsFragment() {
        val fragment = RoundResultFragment()
        startFragment(fragment)
    }

    private fun startNextRound() {
        startActivity(Intent(this, RoundActivity::class.java))
        Timber.d("Start next round")
        finish()
    }

    private fun startGameResults() {
        startActivity(Intent(this, GameResultActivity::class.java))
        Timber.d("No more rounds ")
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
}
