package com.example.sergey.shlypa2.screens.game_settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.TypesArrayAdapter
import com.example.sergey.shlypa2.beans.Type
import com.example.sergey.shlypa2.extensions.extraNotNull
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.game.RoundActivity
import com.example.sergey.shlypa2.screens.words_in.WordsInActivity
import kotlinx.android.synthetic.main.activity_game_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class GameSettingsActivity : AppCompatActivity() {

    companion object {
        private const val REPEAT_GAME = "repeat_game"
        fun getIntent(context: Context, repeat: Boolean = false) =
                Intent(context, GameSettingsActivity::class.java).apply {
                    putExtra(REPEAT_GAME, repeat)
                }
    }

    private val viewModel by viewModel<GameSettingsViewModel>()

    private val repeat by extraNotNull(REPEAT_GAME, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        initToolbar()

        if (repeat) {
            viewModel.onFinish()
            return
        }

        initSeekbar()
        initSwitches()

        initSubscription()

        btNextSettings.setOnClickListener {
            viewModel.onFinish()
        }

    }

    private fun initSubscription() {
        viewModel.typesLiveData.observeSafe(this) { onTypes(it) }

        viewModel.selectedType.observeSafe(this) {
            onSelectedType(it)
        }

        viewModel.startNextActivity.observeSafe(this) {
            when (it) {
                GameSettingsViewModel.StartActivity.START_GAME -> {
                    onStartActivity(RoundActivity())
                }
                GameSettingsViewModel.StartActivity.WORD_IN -> {
                    onStartActivity(WordsInActivity())
                }
                else -> {
                    Timber.e(it.toString())
                }
            }
        }
    }

    private fun onTypes(types: List<Type>) {
        val typesAdapter = TypesArrayAdapter(this, android.R.layout.simple_list_item_1, types.toTypedArray())
        spinnerDificult.adapter = typesAdapter

        spinnerDificult.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.setDifficulty(types[position])
            }
        }
    }

    private fun onSelectedType(type: Type) {
        val position = (spinnerDificult.adapter as? TypesArrayAdapter)?.getPosition(type)
        position?.let { spinnerDificult.setSelection(it) }
    }


    private fun initSwitches() {
        switchSettingAllowRandom.setChecked(viewModel.getAllowRandom())
                .setOnCheckedListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
                    viewModel.setAllowRandom(isChecked)
                })

        ssPenalty.setChecked(viewModel.getMinusBal())
                .setOnCheckedListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
                    viewModel.setMinusBal(isChecked)
                })

        switchSettingAddAllWordRandom.setChecked(viewModel.getAllWorldRandom())
                .setOnCheckedListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
                    viewModel.setAllWorldRandom(isChecked)
                })
    }

    private fun initSeekbar() {

        ssbTurnTime.setValues(Constants.MIN_ROUND_TIME, Constants.MAX_ROUMD_TIME)
                .setProgress(viewModel.getTime())
                .setProgressListener { progress ->
                    viewModel.setTime(progress)
                }

        ssbWordsCount.setValues(Constants.MIN_WORDS_COUNT, Constants.MAX_WORDS_COUNT)
                .setProgress(viewModel.getWordsCount())
                .setProgressListener { progress ->
                    viewModel.setWordsLD(progress)
                }

        ssbPenalty.setValues(Constants.MIN_MINUS_BAL, Constants.MAX_MINUS_BAL)
                .setProgress(viewModel.getPenaltyPoint())
                .setProgressListener { progress ->
                    viewModel.setPenaltyPoint(progress)
                }
    }

    private fun onStartActivity(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
    }

    private fun initToolbar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.savedSettings()
    }
}
