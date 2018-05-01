package com.example.sergey.shlypa2.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.utils.SoundManager
import com.github.florent37.kotlin.pleaseanimate.please
import kotlinx.android.synthetic.main.activity_game_result.*
import java.util.*

class GameResultActivity : AppCompatActivity() {

    private val soundManager = SoundManager(this)

    val animation by lazy {
        please {
            animate(tv_winner){
                leftOfHisParent(10f)
                belowOf(tvGameWinner)

            }
            animate(civWinnerAvatar){
                belowOf(tvGameWinner,4f)
                rightOfHisParent(10f)
                scale(0.3f, 0.3f)
            }
            animate(tv_game_resalt){
                belowOf(tv_winner,null)
                visible()
            }
            animate(rvGameResults){
                belowOf(tv_game_resalt)
                aboveOf(btCreateNewGame)

              //  centerBetweenViews(tv_game_resalt,btCreateNewGame,false,true)

                visible()
            }

        }

    }

    var animations = false
    val resultsAdapter = RvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
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

        civWinnerAvatar.setOnClickListener {
            animations = if (animations) {
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
    }
}

