package com.example.sergey.shlypa2.screens.game_result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.game.adapter.ItemTeamWithScores
import com.example.sergey.shlypa2.screens.game_result.GameResultViewModel.Command
import com.example.sergey.shlypa2.screens.game_settings.GameSettingsActivity
import com.example.sergey.shlypa2.screens.main.FirstActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.activity_game_result.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class GameResultActivity : AppCompatActivity() {

    private val viewModel: GameResultViewModel by viewModel()
    private var animated = false
    private val adapter = FlexibleAdapter(emptyList())
    private val playersPool = RecyclerView.RecycledViewPool()

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        viewModel.onActivityCreated()

        rvGameResults.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvGameResults.adapter = adapter

        viewModel.teamsLiveData.observeSafe(this) { teams ->
            adapter.updateDataSet(teams.map { ItemTeamWithScores(it, playersPool) })
        }

        viewModel.winnerNameLiveData.observeSafe(this) {
            tvWinner.text = it
        }

        viewModel.commandLiveData.observeSafe(this) { command ->
            when (command) {
                Command.RUN_ANIMATION -> runAnimation()
                Command.START_GAME_SETTINGS -> {
                    startActivity(GameSettingsActivity.getIntent(this@GameResultActivity, true)
                            .apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP })
                    finish()
                }
                Command.START_MAIN_ACTIVITY -> {
                   startMain()
                }
                else->{}
            }
        }

        btCreateNewGame.setOnClickListener {
           // dialogRepeatGame()
            startMain()
        }

        civWinnerAvatar.setOnClickListener {
            runAnimation()
        }
    }

    private fun runAnimation() {
        if (!animated) {
            animated = true
            rootGameResult.setTransition(R.id.start, R.id.end)
            rootGameResult.transitionToEnd()
        }
    }

    private fun startMain(){
        startActivity(
                FirstActivity.getIntent(this@GameResultActivity, true)
                        .apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP }
        )
        finish()
    }

    private fun dialogRepeatGame() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.repeatTitle)
            setMessage(R.string.repaetMessage)
            setPositiveButton(R.string.repeatPositive) { dialog, _ ->
                dialog.dismiss()
                viewModel.onRepeatGameClicked()
            }
            setNegativeButton(R.string.repeatNegative) { dialog, _ ->
                dialog.dismiss()
                viewModel.onFinishGameClicked()
            }
        }
                .create()
                .show()
    }

    override fun onBackPressed() {
        //dialogRepeatGame()
        startMain()
    }
}

