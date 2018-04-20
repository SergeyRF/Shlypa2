package com.example.sergey.shlypa2.ui

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.viewModel.PlayersViewModel
import kotlinx.android.synthetic.main.activity_teem.*

class TeamActivity : AppCompatActivity() {

    lateinit var viewModel: PlayersViewModel
    lateinit var adapterTeam: RvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teem)

        viewModel = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        viewModel.getTeamsLiveData().observe(this, Observer { list -> setTeemRv(list) })
        viewModel.initTeams()

        val button = findViewById<Button>(R.id.cancel_teem)
        button.setOnClickListener(View.OnClickListener {

            startActivity(Intent(this, WordsInActivity::class.java))
        })

        adapterTeam = RvAdapter()
        rvTeam.layoutManager = LinearLayoutManager(this)
        rvTeam.adapter = adapterTeam

        adapterTeam.listener = { team: Any ->
            dialog(team as Team)
        }
    }

    fun dialog(team: Team) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_text)
        val etTeemD = dialog.findViewById<EditText>(R.id.etDialog)
        val btYesD = dialog.findViewById<Button>(R.id.btYesDialog)
        val btNoD = dialog.findViewById<Button>(R.id.btNoDialog)
        etTeemD.hint = team.name
        btYesD.setOnClickListener {
            if (etTeemD.text.isNotEmpty()) {
                team.name = etTeemD.text.toString()
                adapterTeam.notifyDataSetChanged()
                dialog.cancel()
            }
        }
        btNoD.setOnClickListener { dialog.cancel() }
        dialog.show()
    }

    fun setTeemRv(teem: List<Team>?) {
        adapterTeam.setData(teem)
    }

}
