package com.example.sergey.shlypa2.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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
                belowOf(civWinnerAvatar,10f)
                visible()
            }
            animate(rvGameResults){
                belowOf(tv_game_resalt)
                visible()
            }

        }

    }

    var animations = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        val resultsAdapter = RvAdapter()
        resultsAdapter.setData(Game.getGameResults())

        soundManager.play(R.raw.fanfair)

        val t:List <TeamWithScores>  = Game.getGameResults().sortedBy { it.scores.and(it.scores) }
       val tm= t.maxBy { it.scores }!!
        tv_winner.text = tm.team.name



        rvGameResults.layoutManager = LinearLayoutManager(this)
        rvGameResults.adapter = resultsAdapter
        btCreateNewGame.setOnClickListener {
            startActivity(Intent(this,FirstActivity::class.java))
        }

        civWinnerAvatar.setOnClickListener {
            animations = if (animations){
                animation.reset()
                false
            }
            else{
                animation.start()
                true
            }
        }
        please(3) {
            animate(tv_game_resalt) {
                invisible()
            }
            animate(rvGameResults){
                invisible()
            }
        }.start()
    }
}

