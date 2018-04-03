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
import android.widget.RadioButton
import android.widget.Toast
import com.example.sergey.shlypa2.PlayerAdapter
import com.example.sergey.shlypa2.viewModel.PlayersViewModel
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.game.Player

class PlayersActivity : AppCompatActivity() {

    lateinit var adapter : PlayerAdapter

    lateinit var viewModel : PlayersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)
        val editName = findViewById<EditText>(R.id.EtName)
        val buttonName = findViewById<RadioButton>(R.id.radioButton)
        val playerRv = findViewById<RecyclerView>(R.id.list_playrs)
        val buttonNext = findViewById<Button>(R.id.goNext)
        adapter = PlayerAdapter()
        val linLayout = LinearLayoutManager(this)
        linLayout.stackFromEnd = true
        linLayout.reverseLayout = true
        playerRv.layoutManager = linLayout
        playerRv.adapter=adapter
        viewModel = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        viewModel.getPlayersLiveData().observe(this, Observer { list -> onPlayersChanged(list) });
        for (i in 0..10){
            viewModel.addPlayer(Player("FAPPPP$i"))
        }

        buttonName.setOnClickListener(View.OnClickListener {
            if(editName.text.isNotEmpty()){
                if(viewModel.addPlayer(Player(editName.text.toString()))){
                }
                else {
                    Toast.makeText(this,"Bje bilo eto imyachko",Toast.LENGTH_LONG).show()
                }
                editName.setText("")
            }
        else Toast.makeText(this,"Pishi Imya",Toast.LENGTH_LONG).show() })

        buttonNext.setOnClickListener(View.OnClickListener {
            if (Game.getPlayers().size<4){
                Toast.makeText(this,"Malo Igrokov",Toast.LENGTH_LONG).show()
            }
            else startActivity( Intent(this, CommandActivity::class.java))
        })
    }

    private fun onPlayersChanged(players: List<Player>?) {
        adapter.setPeople(players)
    }
}
