package com.example.sergey.shlypa2.screens.game_settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.TypesArrayAdapter
import com.example.sergey.shlypa2.beans.Type
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.ui.WordsInActivity
import com.example.sergey.shlypa2.utils.Functions
import kotlinx.android.synthetic.main.activity_game_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class GameSettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<GameSettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setThemeApi21(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        ssbTurnTime.setValues(Constants.MIN_ROUND_TIME, Constants.MAX_ROUMD_TIME)
        ssbTurnTime.setProgress(viewModel.getTime())
        ssbTurnTime.seekbarListener = { _, progress, _ ->
            viewModel.setTime(progress)
        }

        ssbWordsCount.setValues(Constants.MIN_WORDS_COUNT, Constants.MAX_WORDS_COUNT)
        ssbWordsCount.setProgress(viewModel.getWordsCount())
        ssbWordsCount.seekbarListener = { _, progress, _ ->
            viewModel.setWordsLD(progress)
        }

        ssbPenalty.setValues(Constants.MIN_MINUS_BAL, Constants.MAX_MINUS_BAL)
        ssbPenalty.setProgress(viewModel.getnumberMinusBal())
        ssbPenalty.seekbarListener = { _, progress, _ ->
            viewModel.setnumberMInusBal(progress)
        }

        onSwitch(viewModel.getAllowRandom())
        onBalSwitch(viewModel.getMinusBal())

        btNextSettings.setOnClickListener {
            acceptSettings()
            startActivity(Intent(this, WordsInActivity::class.java))
        }

        viewModel.typesLiveData.observeSafe(this) { onTypes(it)}
    }

    private fun onTypes(types: List<Type>) {
        val typesAdapter = TypesArrayAdapter(this, android.R.layout.simple_list_item_1, types.toTypedArray())
        spinnerDificult.adapter = typesAdapter
        viewModel.selectedType?.let { onSelectedType(it) }
    }

    private fun acceptSettings() {
        viewModel.setAllowRandom(switchSettingAllowRandom.isChecked())
        viewModel.setDifficulty(spinnerDificult.selectedItem as Type)
        viewModel.setMinusBal(ssPenalty.isChecked())
        viewModel.onFinish()
    }

    private fun onSelectedType(type: Type) {
        (spinnerDificult.adapter as? TypesArrayAdapter)?.getPosition(type)
                ?.let { spinnerDificult.setSelection(it) }
    }

    private fun onSwitch(b: Boolean) {
        Timber.d("$b")
        switchSettingAllowRandom.setChecked(b)
    }

    private fun onBalSwitch(b: Boolean) {
        ssPenalty.setChecked(b)
    }
}
