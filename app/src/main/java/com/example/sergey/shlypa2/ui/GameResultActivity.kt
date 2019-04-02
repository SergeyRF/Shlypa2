package com.example.sergey.shlypa2.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.extensions.gone
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.extensions.show
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.screens.main.FirstActivity
import com.example.sergey.shlypa2.utils.SoundManager
import com.github.florent37.kotlin.pleaseanimate.please
import kotlinx.android.synthetic.main.activity_game_result.*

class GameResultActivity : AppCompatActivity() {

    private val soundManager = SoundManager(this)

    val animation by lazy {
        please {

            animate(civWinnerAvatar) {
                belowOf(tvGameWinner, 4f)
                rightOfHisParent(10f)
                scale(0.3f, 0.3f)
            }

            animate(tv_winner) {
                leftOfHisParent(10f)
                scale(0.7f, 0.7f)
                alignTop(civWinnerAvatar)

            }
            animate(tv_game_resalt) {
                belowOf(civWinnerAvatar, null)
                visible()
                tv_game_resalt.show()
            }
            animate(rvGameResults) {
                belowOf(tv_game_resalt)
                aboveOf(btCreateNewGame)
                rvGameResults.show()

                visible()
            }

        }

    }

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
            animated = if (animated) {
                animation.reset()
                false
            } else {
                animation.start()
                true
            }
            resultsAdapter.notifyDataSetChanged()
        }

        please(3) {
            animate(tv_game_resalt) {
                invisible()

            }
            animate(rvGameResults) {
                invisible()

            }
        }.start()

        Handler().postDelayed({
            if (!animated) {
                animated = true
                animation.start()
            }
        }, 3000)
    }
}

