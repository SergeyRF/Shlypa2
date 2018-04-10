package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.viewModel.PlayerWordsModel

class WordsInActivity : AppCompatActivity() {

    lateinit var editWord: EditText
    lateinit var rvWord:RecyclerView
    lateinit var player:TextView
    private lateinit var wordsAdapter: RvAdapter
    lateinit var viewStateModel: PlayerWordsModel
    lateinit var buttonKnock:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words_in)
        editWord = findViewById(R.id.wordIn)

        rvWord = findViewById(R.id.RvWords)
        buttonKnock = findViewById(R.id.knock)
        player = findViewById(R.id.playerWho)
        wordsAdapter = RvAdapter()
        rvWord.layoutManager=LinearLayoutManager(this)
        rvWord.adapter = wordsAdapter
        viewStateModel = ViewModelProviders.of(this).get(PlayerWordsModel::class.java)
        viewStateModel.getWordPlayer().observe(this, Observer { list->setWordRv(list) })
        viewStateModel.getPlayer().observe(this, Observer { setPlayer(it) })
        buttonKnock.setOnClickListener(View.OnClickListener {
            if (!viewStateModel.GoGame()) {
                if (viewStateModel.needWord()) {
                    if (editWord.text.toString().isNotEmpty()) {
                        viewStateModel.addWordLD(editWord.text.toString())
                        editWord.text.clear()
                    }
                } else {

                    viewStateModel.addWordGameLD()
                }
            }
            else{
                Game.beginNextRound()
                startActivity(Intent(this, RoundActivity::class.java) )
            }

        })

        var btStart : Button = findViewById(R.id.btBeginGame)

        btStart.setOnClickListener {
            addFakeWords()
            Game.beginNextRound()
            startActivity(Intent(this, RoundActivity::class.java)) }
    }

    fun setWordRv(words :List<Word>?){
        wordsAdapter.setData(words)
    }
    fun setPlayer(p: Player?){
        player.text = p!!.name
    }

    fun addFakeWords() {
        for(player in Game.getPlayers()) {
            for (i in 0..5) {
                Game.addWord(Word("Word $i", addedBy = player.id))
            }
        }
    }
}
