package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.TypesArrayAdapter
import com.example.sergey.shlypa2.game.WordType
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_game_settings.*
import timber.log.Timber


class GameSettingsActivity : AppCompatActivity() {

    lateinit var settingsVM: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        settingsVM = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        ssbTurnTime.setValues(Constants.MIN_ROUND_TIME, Constants.MAX_ROUMD_TIME)
        ssbTurnTime.setProgress(settingsVM.getTime())
        ssbTurnTime.seekbarListener = { _, progress, _ ->
            settingsVM.setTime(progress)
        }

        ssbWordsCount.setValues(Constants.MIN_WORDS_COUNT, Constants.MAX_WORDS_COUNT)
        ssbWordsCount.setProgress(settingsVM.getWordsCount())
        ssbWordsCount.seekbarListener = { _, progress, _ ->
            settingsVM.setWordsLD(progress)
        }

        ssbPenalty.setValues(Constants.MIN_MINUS_BAL, Constants.MAX_MINUS_BAL)
        ssbPenalty.setProgress(settingsVM.getnumberMinusBal())
        ssbPenalty.seekbarListener = { _, progress, _ ->
            settingsVM.setnumberMInusBal(progress)
        }

        onSwitch(settingsVM.getAllowRandom())
        onBalSwitch(settingsVM.getMinusBal())


        val button = findViewById<Button>(R.id.bt_go_next)
        button.setOnClickListener {
            startActivity(Intent(this, WordsInActivity::class.java))
        }


        val typesList = arrayOf(WordType.EASY, WordType.MEDIUM, WordType.HARD, WordType.VERY_HARD)
        val adapter = TypesArrayAdapter(this, android.R.layout.simple_list_item_1, typesList)

        spinnerDificult.adapter = adapter

        onDificult(settingsVM.getDifficulty())
    }

    override fun onStop() {
        super.onStop()
        settingsVM.setAllowRandom(switchSettingAllowRandom.isChecked())
        settingsVM.setDifficulty(spinnerDificult.selectedItem as WordType)
        settingsVM.setMinusBal(ssPenalty.isChecked())
        settingsVM.onFinish()
    }

    private fun onDificult(d: WordType?) {
        Timber.d("Difficulty $d position ${WordType.values().indexOf(d)}")
        spinnerDificult.setSelection(WordType.values().indexOf(d))
    }

    private fun onSwitch(b: Boolean) {
        Timber.d("$b")
        switchSettingAllowRandom.setChecked(b)
    }

    private fun onBalSwitch(b: Boolean) {
        ssPenalty.setChecked(b)
    }
}
