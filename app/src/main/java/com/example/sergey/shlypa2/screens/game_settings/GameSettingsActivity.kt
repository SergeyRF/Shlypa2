package com.example.sergey.shlypa2.screens.game_settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.extraNotNull
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.game.RoundActivity
import com.example.sergey.shlypa2.screens.game_settings.items.ItemSeekBar
import com.example.sergey.shlypa2.screens.game_settings.items.ItemSpinner
import com.example.sergey.shlypa2.screens.game_settings.items.ItemSwitch
import com.example.sergey.shlypa2.screens.words_in.WordsInActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.activity_game_settings.btCompletedSettings
import kotlinx.android.synthetic.main.activity_game_settings_new.*
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

    private val adapter = FlexibleAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings_new)

        initToolbar()

        if (repeat) {
            viewModel.onFinish()
            return
        }

        rvSettings.layoutManager = LinearLayoutManager(this)
        rvSettings.adapter = adapter

        initSubscription()

        btCompletedSettings.setOnClickListener {
            viewModel.onFinish()
        }

    }

    private fun initItems() {

        val timeItem = ItemSeekBar(
                getString(R.string.timeN),
                Constants.MIN_ROUND_TIME,
                Constants.MAX_ROUMD_TIME,
                viewModel.getTime())
        { progress ->
            viewModel.setTime(progress)
        }

        val wordPointItem = ItemSeekBar(
                getString(R.string.wordN),
                Constants.MIN_WORDS_COUNT,
                Constants.MAX_WORDS_COUNT,
                viewModel.getWordsCount())
        { progress ->
            viewModel.setWordsLD(progress)
        }

        val autoFillHatItem = ItemSwitch(
                getString(R.string.add_all_word_random),
                viewModel.getAllWorldRandom()
        ) { isChecked ->
            viewModel.setAllWorldRandom(isChecked)
        }

        val allowRandomWord = ItemSwitch(
                getString(R.string.allow_random),
                viewModel.getAllowRandom()
        ) { allowRandom ->
            viewModel.setAllowRandom(allowRandom)
        }

        val difficultItem = ItemSpinner(
                getString(R.string.dificult),
                viewModel.getTypesList(),
                viewModel.getTypeSelected()
        ) { type ->
            viewModel.setDifficulty(type)
        }

        val penaltyItem = ItemSwitch(
                getString(R.string.penalty_include),
                viewModel.getPenaltyInclude()
        ) { penalty ->
            viewModel.setPenaltyInclude(penalty)
        }

        val penaltyPointItem = ItemSeekBar(
                getString(R.string.penalty_point),
                Constants.MIN_MINUS_BAL,
                Constants.MAX_MINUS_BAL,
                viewModel.getPenaltyPoint()
        ) { penaltyPoint ->
            viewModel.setPenaltyPoint(penaltyPoint)
        }

        val items = mutableListOf<IFlexible<*>>(
                timeItem,
                wordPointItem,
                autoFillHatItem,
                allowRandomWord,
                difficultItem,
                penaltyItem,
                penaltyPointItem)

        adapter.clear()
        adapter.addItems(0, items)

    }

    private fun initSubscription() {
        viewModel.waitLoadingTypes.observeSafe(this) {
            initItems()
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
