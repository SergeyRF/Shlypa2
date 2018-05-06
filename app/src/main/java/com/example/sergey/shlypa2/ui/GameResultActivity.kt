package com.example.sergey.shlypa2.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.utils.*
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import com.github.florent37.kotlin.pleaseanimate.please
import kotlinx.android.synthetic.main.activity_game_result.*

class GameResultActivity : AppCompatActivity() {

    private val soundManager = SoundManager(this)

    val animation by lazy {
        please {
            animate(tv_winner) {
                leftOfHisParent(10f)
                belowOf(tvGameWinner)

            }
            animate(civWinnerAvatar) {
                belowOf(tvGameWinner, 4f)
                rightOfHisParent(10f)
                scale(0.3f, 0.3f)
            }
            animate(tv_game_resalt) {
                belowOf(tv_winner, null)
                visible()
                tv_game_resalt.show()
            }
            animate(rvGameResults) {
                belowOf(tv_game_resalt)
                aboveOf(btCreateNewGame)
                rvGameResults.show()
                //  centerBetweenViews(tv_game_resalt,btCreateNewGame,false,true)

                visible()
            }

        }

    }

    var animated = false
    val resultsAdapter = RvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)


        soundManager.play(R.raw.fanfair)

        val resultsList: List<TeamWithScores> = Game.getGameResults().sortedByDescending { it.getScores() }
        resultsAdapter.setData(resultsList)

        val tm = resultsList.maxBy { it.getScores() }!!
        tv_winner.text = tm.team.name

        rvGameResults.layoutManager = LinearLayoutManager(this)
        rvGameResults.adapter = resultsAdapter
        btCreateNewGame.setOnClickListener {
            startActivity(Intent(this, FirstActivity::class.java))
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

