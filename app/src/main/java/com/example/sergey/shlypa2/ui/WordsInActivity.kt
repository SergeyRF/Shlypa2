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
import com.example.sergey.shlypa2.utils.gone
import com.example.sergey.shlypa2.utils.hide
import com.example.sergey.shlypa2.utils.show
import com.example.sergey.shlypa2.viewModel.PlayerWordsModel
import kotlinx.android.synthetic.main.activity_words_in.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast


class WordsInActivity : AppCompatActivity() {


    private lateinit var wordsAdapter: RvAdapter
    lateinit var viewStateModel: PlayerWordsModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words_in)

        wordsAdapter = RvAdapter()
        rvWords.layoutManager=LinearLayoutManager(this)
        rvWords.adapter = wordsAdapter

        viewStateModel = ViewModelProviders.of(this).get(PlayerWordsModel::class.java)

        viewStateModel.getWordsLiveData().observe(this, Observer { list->setWordRv(list) })

        viewStateModel.getPlayerLiveData().observe(this, Observer { setPlayer(it) })

        viewStateModel.needWord.observe(this, Observer { needWords -> if(needWords != null) onNeedWordsChanged(needWords) })

        viewStateModel.inputFinishCallBack.observe(this, Observer { bool ->
            if(bool != null && bool) onStartGame() })

        ibAddWord.setOnClickListener{
                if (etWord.text.toString().isNotEmpty()) {
                    viewStateModel.addWord(etWord.text.toString())
                    etWord.text.clear()
                }
        }
        etWord.setOnEditorActionListener { v, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_NEXT&&etWord.text.isNotEmpty()) {
                // обработка нажатия Enter
                viewStateModel.addWord(etWord.text.toString())
                etWord.text.clear()
                true
            } else true
        }


        btNext.setOnClickListener{
            if (viewStateModel.needWord() && viewStateModel.randomAllowed()){
                viewStateModel.fillWithRandomWords()
            } else{
                viewStateModel.nextPlayer()
                etWord.text.clear()
            }
        }

        wordsAdapter.listener = {word:Any->
            // dialog(word as Word)
            viewStateModel.reNameWord(word as Word)
        }

        wordsAdapter.listenerTwo = {word:Any->
            viewStateModel.deleteWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
        }

        wordsAdapter.listenerThree = {word:Any->
            viewStateModel.newRandomWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
        }


    }


    fun setWordRv(words :List<Word>?){
        wordsAdapter.setData(words)
    }
    fun setPlayer(p: Player?){
        wordInject.text = p!!.name
        setTitle(p!!.name)
    }

    fun onNeedWordsChanged(needWords : Boolean){
        if (needWords){
            Toast.makeText(this, "Слов осталось ввести ${viewStateModel.needWordSize()}",
                    Toast.LENGTH_LONG).show()
            if (viewStateModel.randomAllowed()){
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
        tvRename.setText(R.string.word_rename)
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
