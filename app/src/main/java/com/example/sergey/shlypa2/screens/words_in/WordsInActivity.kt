package com.example.sergey.shlypa2.screens.words_in

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.extensions.*
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.screens.game.RoundActivity
import com.example.sergey.shlypa2.utils.anal.AnalSender
import kotlinx.android.synthetic.main.activity_words_in.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class WordsInActivity : AppCompatActivity() {


    private lateinit var wordsAdapter: RvAdapter
    private val viewModel by viewModel<WordsViewModel>()
    private val anal by inject<AnalSender>()

    private var animated = false


    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words_in)

        initToolbar()

        container.setTransition(R.id.start, R.id.end)

        wordsAdapter = RvAdapter()
        rvWords.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvWords.adapter = wordsAdapter
        if (!viewModel.randomAllowed()) wordsAdapter.offChangeWordFlag()

        Timber.d("random allowed ${viewModel.randomAllowed()}")

        viewModel.getWordsLiveData().observe(this, Observer { list -> setWordRv(list) })

        viewModel.getPlayerLiveData().observe(this, Observer { setPlayer(it) })

        viewModel.getNeedWordsLive()
                .observe(this, Observer { needWords ->
                    needWords?.let { onNeedWordsChanged(it) }
                })

        viewModel.inputFinishCallBack.observeSafe(this) { bool ->
            if (bool) onStartGame()
        }

        ibAddWord.setOnClickListener {
            if (etWord.text.toString().isNotEmpty()) {
                viewModel.addWord(etWord.text.toString())
                etWord.text.clear()
            }
        }

        etWord.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT && etWord.text.isNotEmpty()) {
                // обработка нажатия Enter
                viewModel.addWord(etWord.text.toString())
                etWord.text.clear()
                true
            } else true
        }

        btNextPlayer.setOnClickListener {
            if (viewModel.needWord() && viewModel.randomAllowed()) {
                viewModel.fillWithRandomWords()
            } else {
                viewModel.nextPlayer()
                etWord.text.clear()
                etWord.showKeyboard()
            }
        }

        wordsAdapter.listenerTwo = { word: Any ->
            viewModel.deleteWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
        }

        wordsAdapter.listenerThree = { word: Any ->
            viewModel.newRandomWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
        }

        wordsAdapter.setData(listOf(Word("${viewModel.randomAllowed()}"), Word("hello2")))

        onChangeEt()
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

    private fun setWordRv(words: List<Word>?) {
        if (words == null || words.isEmpty()) {
            if (animated) {
                hideList()
                animated = false
            }
        } else {
            if (!animated) {
                showList()
                animated = true
            }

        }

        wordsAdapter.setData(words)
    }

    private fun showList() {
        container.transitionToEnd()
    }

    private fun hideList() {
        container.transitionToStart()
    }

    fun setPlayer(p: Player?) {
        p?.let {
            //            title = p.name
            Glide.with(this)
                    .load(p.getLargeImage(this))
                    .into(civPlayerAvatar)

            tvWhoWrites.text = getString(R.string.who_inputs, p.name)
        }
    }

    private fun onNeedWordsChanged(needWords: Boolean) {
        if (needWords) {
            onChangeEt()

            if (viewModel.randomAllowed()) {
                btNextPlayer.text = getString(R.string.add_random)
            } else {
                btNextPlayer.gone()
            }
            ibAddWord.show()
            etWord.show()
        } else {
            hideKeyboard()
            ibAddWord.gone()
            etWord.gone()
            btNextPlayer.show()
            btNextPlayer.text = getString(R.string.play)
        }
    }

    private fun onChangeEt() {
        etWord.hint = getString(R.string.words_input_left, viewModel.needWordSize())
    }


    private fun onStartGame() {
        anal.gameStarted(Game.getSettings().allowRandomWords, Game.getSettings().typeName)
        Game.beginNextRound()
        startActivity(Intent(this, RoundActivity::class.java))
        finish()
    }
}
