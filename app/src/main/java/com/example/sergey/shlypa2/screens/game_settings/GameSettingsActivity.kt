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
import com.example.sergey.shlypa2.screens.game_settings.items.ItemPenalty
import com.example.sergey.shlypa2.screens.game_settings.items.ItemSeekBar
import com.example.sergey.shlypa2.screens.game_settings.items.ItemWord
import com.example.sergey.shlypa2.screens.words_in.WordsInActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
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

    private val adapter = FlexibleAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        initToolbar()

        initSubscription()

        btCompletedSettings.setOnClickListener {
            viewModel.onFinish()
        }

        rvSettings.layoutManager = LinearLayoutManager(this)
        rvSettings.adapter = adapter

        if (repeat) {
            viewModel.onFinish()
            return
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

        val itemRandom = ItemWord(
                viewModel.getWordsSettings(),
                { autoFill -> viewModel.setAutoFill(autoFill) },
                { allowRandom -> viewModel.setAllowRandom(allowRandom) },
                { type -> viewModel.setDifficulty(type) })

        val itemPenalty = ItemPenalty(
                viewModel.getPenalty(),
                { include -> viewModel.setPenaltyInclude(include) },
                { point -> viewModel.setPenaltyPoint(point) })

        val items = mutableListOf<IFlexible<*>>(
                timeItem,
                wordPointItem,
                itemPenalty,
                itemRandom)

        adapter.clear()
        adapter.addItems(0, items)
        adapter.expandItemsAtStartUp()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
