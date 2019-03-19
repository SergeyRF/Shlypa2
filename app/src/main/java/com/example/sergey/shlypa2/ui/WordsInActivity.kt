package com.example.sergey.shlypa2.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.EditorInfo
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.extensions.*
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.utils.*
import com.example.sergey.shlypa2.viewModel.WordsViewModel
import com.github.florent37.kotlin.pleaseanimate.please
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_words_in.*
import timber.log.Timber


class WordsInActivity : AppCompatActivity() {


    private lateinit var wordsAdapter: RvAdapter
    lateinit var viewModel: WordsViewModel

    private var animated = false

    private val avatarHideAnimation by lazy {
        please(300, interpolator = AccelerateInterpolator()) {
            animate(civPlayerAvatar) toBe {
                outOfScreen(Gravity.LEFT)
            }
            animate(tvWhoWrites) toBe {
                outOfScreen(Gravity.LEFT)
            }

            animate(rvWords) toBe {
                originalPosition()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setThemeApi21(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words_in)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        wordsAdapter = RvAdapter()
        rvWords.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvWords.adapter = wordsAdapter

        viewModel = ViewModelProviders.of(this).get(WordsViewModel::class.java)

        Timber.d("random allowed ${viewModel.randomAllowed()}")

        viewModel.getWordsLiveData().observe(this, Observer { list -> setWordRv(list) })

        viewModel.getPlayerLiveData().observe(this, Observer { setPlayer(it) })

        viewModel.getNeedWordsLive()
                .observe(this, Observer { needWords ->
                    needWords?.let { onNeedWordsChanged(it) }
                })

        viewModel.inputFinishCallBack.observe(this, Observer { bool ->
            if (bool != null && bool) onStartGame()
        })

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

        btNextWords.setOnClickListener {
            if (viewModel.needWord() && viewModel.randomAllowed()) {
                viewModel.fillWithRandomWords()
            } else {
                viewModel.nextPlayer()
                etWord.text.clear()
//                etWord.requestFocus()
                etWord.showKeyboard()
            }
        }

        wordsAdapter.listener = { word: Any ->
            // dialog(word as Word)
            viewModel.reNameWord(word as Word)
        }

        wordsAdapter.listenerTwo = { word: Any ->
            viewModel.deleteWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
        }

        wordsAdapter.listenerThree = { word: Any ->
            viewModel.newRandomWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
        }

        onChangeEt()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                android.R.id.home -> super.onBackPressed()
            }
        }

        return true
    }

    override fun onStart() {
        super.onStart()
        if(!animated) {
            please(10) {
                animate(rvWords) {
                    outOfScreen(Gravity.RIGHT)
                }
            }.now()
        }
    }


    private fun setWordRv(words: List<Word>?) {
        if (words == null || words.isEmpty()) {
            if (animated) {
                avatarHideAnimation.reset()
                animated = false
            }
        } else {
            if (!animated) {
                avatarHideAnimation.start()
                animated = true
            }
        }

        wordsAdapter.setData(words)
    }

    fun setPlayer(p: Player?) {
        p?.let {
            //            title = p.name
            Picasso.get()
                    .load(Functions.imageNameToUrl("player_avatars/large/${p.avatar}"))
                    .into(civPlayerAvatar)

            tvWhoWrites.text = getString(R.string.who_inputs, p.name)
        }
    }

    private fun onNeedWordsChanged(needWords: Boolean) {
        if (needWords) {
            onChangeEt()

            if (viewModel.randomAllowed()) {
                btNextWords.text = getString(R.string.add_random)
            } else {
                btNextWords.hide()
            }
            ibAddWord.show()
            etWord.show()
        } else {
            hideKeyboard()
            btNextWords.show()
            ibAddWord.gone()
            etWord.gone()
            btNextWords.text = getString(R.string.play)
        }
    }

    private fun onChangeEt() {
        etWord.hint = getString(R.string.words_input_left, viewModel.needWordSize())
    }


    private fun onStartGame() {
        Game.beginNextRound()
        startActivity(Intent(this, RoundActivity::class.java))
        finish()
    }
}
