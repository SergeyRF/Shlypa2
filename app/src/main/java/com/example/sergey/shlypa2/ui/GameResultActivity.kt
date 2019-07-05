package com.example.sergey.shlypa2.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.animate
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.extensions.gone
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.screens.main.FirstActivity
import com.example.sergey.shlypa2.utils.SoundManager
import kotlinx.android.synthetic.main.activity_game_result.*

class GameResultActivity : AppCompatActivity() {

    private val soundManager = SoundManager(this)

    var animated = false
    val resultsAdapter = RvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)


        soundManager.play(R.raw.fanfair)

        val resultsList: List<TeamWithScores> = Game.getGameResults().sortedByDescending { it.getScores() }
        resultsAdapter.setData(resultsList)

        val tm = resultsList.maxBy { it.getScores() }

        tv_winner.text = tm?.team?.name ?: "Unknown"

        rvGameResults.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvGameResults.adapter = resultsAdapter
        btCreateNewGame.setOnClickListener {
            startActivity(FirstActivity.getIntent(this, true))
            finish()
        }

        rvGameResults.gone()
        tv_game_resalt.gone()

        civWinnerAvatar.setOnClickListener {
            runAnimation()
        }

        Handler().postDelayed({
            runAnimation()
        }, 3000)
    }

    private fun runAnimation() {
        if (!animated) {
            animated = true
            rootGameResult.setTransition(R.id.start, R.id.end)
            rootGameResult.transitionToEnd()
        }
    }
}

