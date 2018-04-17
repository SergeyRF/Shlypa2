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
import com.example.sergey.shlypa2.utils.hide
import com.example.sergey.shlypa2.utils.show
import com.example.sergey.shlypa2.viewModel.PlayerWordsModel
import kotlinx.android.synthetic.main.activity_words_in.*
import timber.log.Timber

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

        viewStateModel.inputFinishCallBack.observe(this, Observer { bool ->
            if(bool != null && bool) onStartGame() })
        buttonLook()
        ibAddWord.setOnClickListener{
                if (etWord.text.toString().isNotEmpty()) {
                    viewStateModel.addWord(etWord.text.toString())
                    etWord.text.clear()
                }
                buttonLook()
        }

        btNext.setOnClickListener{
            viewStateModel.nextPlayer()
            etWord.text.clear()
            buttonLook()
        }

        btBeginGame.setOnClickListener {
            addFakeWords()
            Game.beginNextRound()
            startActivity(Intent(this, RoundActivity::class.java)) }

        btRandomWord.setOnClickListener{
            viewStateModel.fillWithRandomWords()
            buttonLook()
        }

        wordsAdapter.listener = {word:Any->
             dialog(word as Word)
        }
        wordsAdapter.listenerTwo = {word:Any->
            viewStateModel.deleteWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
            buttonLook()
        }
        wordsAdapter.listenerThree = {word:Any->
            viewStateModel.renameWord(word as Word)
            wordsAdapter.notifyDataSetChanged()
        }

    }

    fun setWordRv(words :List<Word>?){
        wordsAdapter.setData(words)
    }
    fun setPlayer(p: Player?){
        wordInject.text = p!!.name
    }

    fun buttonLook(){
        if (viewStateModel.needWord()){
            btNext.hide()
            btRandomWord.show()
            ibAddWord.show()
        }
        else{
            btNext.show()
            ibAddWord.hide()
            btRandomWord.hide()
        }
    }

    fun addFakeWords() {
        for(player in Game.getPlayers()) {
            for (i in 0..5) {
                Game.addWord(Word("Word $i", addedBy = player.id))
            }
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
        etTeemD.hint=word.word
        btYesD.setOnClickListener{
            if (etTeemD.text.isNotEmpty()){
                word.word = etTeemD.text.toString()
                wordsAdapter.notifyDataSetChanged()
                dialog.cancel()
            }
        }
        btNoD.setOnClickListener { dialog.cancel() }
        dialog.show()
    }
}
