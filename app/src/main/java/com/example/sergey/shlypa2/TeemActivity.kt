package com.example.sergey.shlypa2

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.Toast

class TeemActivity : AppCompatActivity() {

    lateinit var teamVM: PlayersViewModel
    lateinit var adapterTeam: TeemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teem)
        val teemRv = findViewById<RecyclerView>(R.id.rvTeem)
        adapterTeam = TeemAdapter()
        teemRv.layoutManager=LinearLayoutManager(this)
        teemRv.adapter = adapterTeam
        teamVM = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        teamVM.getTeamsLiveData().observe(this, Observer { list -> setTeemRv(list) })

        Toast.makeText(this, Game.getTeams().size.toString(), Toast.LENGTH_LONG).show()
        val button = findViewById<Button>(R.id.cancel_teem)
        button.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, FirstActivity::class.java))
        })
    }

    fun setTeemRv(teem: List<Team>?) {
        adapterTeam.setTeem(teem)
    }
}
