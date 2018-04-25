package com.example.sergey.shlypa2.ui

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Button
import android.widget.EditText
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.viewModel.WordsViewModel
import kotlinx.android.synthetic.main.activity_words_in.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.example.sergey.shlypa2.utils.*
import com.squareup.picasso.Picasso


class WordsInActivity : AppCompatActivity() {


    private lateinit var wordsAdapter: RvAdapter
    lateinit var viewModel: WordsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words_in)

        wordsAdapter = RvAdapter()
        rvWords.layoutManager=LinearLayoutManager(this)
        rvWords.adapter = wordsAdapter

        viewModel = ViewModelProviders.of(this).get(WordsViewModel::class.java)

        viewModel.getWordsLiveData().observe(this, Observer { list->setWordRv(list) })

        viewModel.getPlayerLiveData().observe(this, Observer { setPlayer(it) })

        viewModel.needWord.observe(this, Observer { needWords -> if(needWords != null) onNeedWordsChanged(needWords) })

        viewModel.inputFinishCallBack.observe(this, Observer { bool ->
            if(bool != null && bool) onStartGame() })

        ibAddWord.setOnClickListener{
                if (etWord.text.toString().isNotEmpty()) {
                    viewModel.addWord(etWord.text.toString())
                    etWord.text.clear()
                }
        }
        etWord.setOnEditorActionListener { v, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_NEXT&&etWord.text.isNotEmpty()) {
                // обработка нажатия Enter
                viewModel.addWord(etWord.text.toString())
                etWord.text.clear()
                true
            } else true
        }


        btNext.setOnClickListener{
            if (viewModel.needWord() && viewModel.randomAllowed()){
                viewModel.fillWithRandomWords()
            } else{
                viewModel.nextPlayer()
                etWord.text.clear()
            }
        }

        wordsAdapter.listener = {word:Any->
            // dialog(word as Word)
            viewModel.reNameWord(word as Word)
        }

        wordsAdapter.listenerTwo = {word:Any->
            viewModel.deleteWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
        }

        wordsAdapter.listenerThree = {word:Any->
            viewModel.newRandomWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
        }


        onChangeEt()
    }


    fun setWordRv(words :List<Word>?){
        if(words == null || words.isEmpty()) {
            playerGroup.show()
        } else {
            playerGroup.hide()
        }
        wordsAdapter.setData(words)
    }
    fun setPlayer(p: Player?){
        p?.let {
//            title = p.name
            Picasso.get()
                    .load(Functions.imageNameToUrl(p.avatar))
                    .into(civPlayerAvatar)

            tvWhoWrites.text = getString(R.string.who_inputs, p.name)
        }
    }

    fun onNeedWordsChanged(needWords : Boolean){
        if (needWords){
            onChangeEt()

            if (viewModel.randomAllowed()){
                btNext.text = getString(R.string.add_random)
            } else{
                btNext.hide()
            }
            ibAddWord.show()
            etWord.show()
        } else{
            btNext.show()
            ibAddWord.gone()
            etWord.gone()
            btNext.text = getString(R.string.play)
        }
    }

    fun onChangeEt() {
        etWord.hint = getString(R.string.words_input_left, viewModel.needWordSize())
    }


    fun onStartGame() {
        Game.beginNextRound()
        startActivity(Intent(this, RoundActivity::class.java))
    }

    fun dialog(word: Word){
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_text)
        val etTeemD = dialog.findViewById<EditText>(R.id.etDialog)
        val btYesD = dialog.findViewById<Button>(R.id.btYesDialog)
        val btNoD = dialog.findViewById<Button>(R.id.btNoDialog)
        val tvRename = dialog.findViewById<TextView>(R.id.tvReNameIt)
        etTeemD.setText(word.word)
        btYesD.setOnClickListener{
            if (etTeemD.text.isNotEmpty()){
                word.word = etTeemD.text.toString()
                wordsAdapter.notifyDataSetChanged()
                dialog.cancel()
            }
        }
        etTeemD.setOnEditorActionListener { v, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_NEXT&&etTeemD.text.isNotEmpty()){
                word.word = etTeemD.text.toString()
                wordsAdapter.notifyDataSetChanged()
                dialog.cancel()
            }
            true
        }
        btNoD.setOnClickListener { dialog.cancel() }
        dialog.show()
    }
}
