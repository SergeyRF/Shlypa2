package com.example.sergey.shlypa2.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.extensions.gone
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.TeamWithScores
import com.example.sergey.shlypa2.screens.game_settings.GameSettingsActivity
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
            dialogRepeatGame()
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

    private fun dialogRepeatGame() {
        AlertDialog.Builder(this).apply {
            setTitle("Игра завершена")
            setMessage("Хотите сыграть еще раз с теми-же настройками?")
            setPositiveButton("Да") { dialog, _ ->
                Game.repeatGame()
                startActivity(GameSettingsActivity.getIntent(this@GameResultActivity, true))
                finish()
            }
            setNegativeButton("Нет") { dialog, _ ->
                startActivity(FirstActivity.getIntent(this@GameResultActivity, true)
                        .apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
                dialog.dismiss()
                finish()
            }
        }
                .create()
                .show()
    }

    override fun onBackPressed() {
        dialogRepeatGame()
    }
}

