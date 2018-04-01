package com.example.sergey.shlypa2

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
        playerRv.layoutManager = LinearLayoutManager(this)
        playerRv.adapter=adapter
        viewModel = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        viewModel.getPlayersLiveData().observe(this, Observer { list -> onPlayersChanged(list) });
        for (i in 0..2){
            viewModel.addPlayer(Player("FAPPPP$i"))
        }

        buttonName.setOnClickListener(View.OnClickListener {
            if(editName.text.isNotEmpty()){
                if(viewModel.addPlayer(Player(editName.text.toString()))){
                }
                else Toast.makeText(this,"Bje bilo eto imyachko",Toast.LENGTH_LONG).show()
            }
        else Toast.makeText(this,"Pishi Imya",Toast.LENGTH_LONG).show() })

        buttonNext.setOnClickListener(View.OnClickListener {
            if (Game.getPlayers().size<4){
                Toast.makeText(this,"Malo Igrokov",Toast.LENGTH_LONG).show()
            }
            else startActivity( Intent(this,CommandActivity::class.java))
        })
    }

    private fun onPlayersChanged(players: List<Player>?) {
        adapter.setPeople(players)
    }
}
