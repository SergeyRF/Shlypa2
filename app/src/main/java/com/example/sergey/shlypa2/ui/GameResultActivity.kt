package com.example.sergey.shlypa2.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.game.Game
import kotlinx.android.synthetic.main.activity_game_result.*

class GameResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        val resultsAdapter = RvAdapter()
        resultsAdapter.setData(Game.getGameResults())

        rvGameResults.layoutManager = LinearLayoutManager(this)
        rvGameResults.adapter = resultsAdapter
    }
}
