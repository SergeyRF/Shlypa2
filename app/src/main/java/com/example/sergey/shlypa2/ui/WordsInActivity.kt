package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.viewModel.PlayerWordsModel
import kotlinx.android.synthetic.main.activity_words_in.*

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

        btNext.setOnClickListener{
            if(viewStateModel.needWord()) {
                if (etWord.text.toString().isNotEmpty()) {
                    viewStateModel.addWord(etWord.text.toString())
                    etWord.text.clear()
                }
            } else viewStateModel.nextPlayer()
        }



        btBeginGame.setOnClickListener {
            addFakeWords()
            Game.beginNextRound()
            startActivity(Intent(this, RoundActivity::class.java)) }

        btRandomWord.setOnClickListener{
            viewStateModel.fillWithRandomWords()
        }

    }

    fun setWordRv(words :List<Word>?){
        wordsAdapter.setData(words)
    }
    fun setPlayer(p: Player?){
        playerName.text = p!!.name
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
}
