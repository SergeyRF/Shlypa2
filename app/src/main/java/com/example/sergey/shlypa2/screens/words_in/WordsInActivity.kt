package com.example.sergey.shlypa2.screens.words_in

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.extensions.*
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.screens.game.RoundActivity
import com.example.sergey.shlypa2.utils.anal.AnalSender
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.activity_words_in.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class WordsInActivity : AppCompatActivity() {

    private val viewModel by viewModel<WordsViewModel>()
    private val anal by inject<AnalSender>()

    private var wordsAdapter = FlexibleAdapter(emptyList())


    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words_in)

        initToolbar()
        container.setTransition(R.id.start, R.id.end)

        initViews()
        initSubscriptions()
    }

    private fun initViews() {
        rvWords.layoutManager = LinearLayoutManager(this)
        rvWords.adapter = wordsAdapter

        ibAddWord.setOnClickListener {
            addWord()
        }

        etWord.onActionNext {
            // обработка нажатия Enter
            addWord()
        }

        btNextPlayer.setOnClickListener {
            viewModel.clickNext()
        }
    }

    private fun initSubscriptions() {

        viewModel.wordsLiveData.observeSafe(this) {
            setWordItem(it.first, it.second)
        }

        viewModel.playerLiveData.observeSafe(this) {
            setPlayer(it)
        }

        viewModel.inputFinishCallBackLD.observeSafe(this) {
            onStartGame()
        }

        viewModel.animateLiveData.observeSafe(this) {
            if (it) showList()
            else hideList()
        }

        viewModel.buttonNextLiveData.observeSafe(this) {
            btNextPlayer.setVisibility(it.first)
            btNextPlayer.setText(it.second)
        }

        viewModel.ediTextChangedLiveData.observeSafe(this) {
            if (it) {
                ibAddWord.show()
                etWord.show()
                etWord.text.clear()
                etWord.showKeyboard()
            } else {
                hideKeyboard()
                ibAddWord.gone()
                etWord.gone()
            }
        }

    }

    private fun addWord() {
        with(etWord.text) {
            if (isNotEmpty()) {
                onChangeEt(viewModel.addWord(this.toString()))
                clear()
            }
        }
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

    private fun setWordItem(listWords: List<Word>, randomAllowed: Boolean) {
        val items = listWords.map { word ->
            WordItem(randomAllowed, word) {
                when (it.first) {
                    WordAct.CHANGE -> {
                        viewModel.changeWord(it.second)
                    }
                    WordAct.DELETE -> {
                        viewModel.deleteWord(it.second)
                    }
                }
            }
        }
        Timber.d("TESTING set words size ${items.size}")
        wordsAdapter.updateDataSet(items)
    }


    private fun showList() {
        container.post { container.transitionToEnd()  }
    }

    private fun hideList() {
        container.transitionToStart()
    }

    private fun setPlayer(p: Player?) {
        p?.let {
            Glide.with(this)
                    .load(p.getLargeImage(this))
                    .into(civPlayerAvatar)

            tvWhoWrites.text = getString(R.string.who_inputs, p.name)
        }
    }

    private fun onChangeEt(needWordSize: Int) {
        etWord.hint = getString(R.string.words_input_left, needWordSize)
    }

    private fun onStartGame() {
        anal.gameStarted(Game.getSettings().allowRandomWords, Game.getSettings().typeName)
        Game.beginNextRound()
        startActivity(Intent(this, RoundActivity::class.java))
        finish()
    }
}
