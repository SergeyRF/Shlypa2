package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ui.fragments.LoadStateFragment
import com.example.sergey.shlypa2.ui.fragments.WelcomeFragment
import com.example.sergey.shlypa2.viewModel.WelcomeViewModel
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : AppCompatActivity() {

    lateinit var viewModel : WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

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
