package com.example.sergey.shlypa2

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class WordsInActivity : AppCompatActivity() {

    lateinit var editWord: EditText
    lateinit var rvWord:RecyclerView
    lateinit var player:TextView
    private lateinit var adpterW: WordAdapter
    lateinit var viewStateModel: StateViewModel
    lateinit var buttonKnock:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words_in)
        editWord = findViewById(R.id.wordIn)

        rvWord = findViewById(R.id.RvWords)
        buttonKnock = findViewById(R.id.knock)
        player = findViewById(R.id.playerWho)
        adpterW = WordAdapter()
        rvWord.layoutManager=LinearLayoutManager(this)
        rvWord.adapter = adpterW
        viewStateModel = ViewModelProviders.of(this).get(StateViewModel::class.java)
        viewStateModel.getWordPlayer().observe(this, Observer { list->setWordRv(list) })

        buttonKnock.setOnClickListener(View.OnClickListener {
            if(editWord.text.toString().isNotEmpty()){
            viewStateModel.addWordLD(editWord.text.toString())
                editWord.text.clear()
        }
        })
    }

    fun setWordRv(w:List<Word>?){
        adpterW.setWords(w)
    }
}
