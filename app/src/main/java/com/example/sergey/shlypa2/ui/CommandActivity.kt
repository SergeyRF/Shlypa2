package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.viewModel.StateViewModel

class CommandActivity : AppCompatActivity() {

    lateinit var stateVM: StateViewModel
    lateinit var time: TextView
    lateinit var words: TextView
    lateinit var commands: TextView
    var teamNeed: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_command)
        time = findViewById<TextView>(R.id.time)
        val timePlus = findViewById<Button>(R.id.plusTime)
        val timeMinus = findViewById<Button>(R.id.minusTime)

        words = findViewById<TextView>(R.id.word)
        val wordMinus = findViewById<Button>(R.id.minusWord)
        val wordPlus = findViewById<Button>(R.id.plusWord)
        val minusButton = findViewById<Button>(R.id.minusCommand)
        val plusButton = findViewById<Button>(R.id.plusCommand)
        commands = findViewById<TextView>(R.id.commands)

        stateVM = ViewModelProviders.of(this).get(StateViewModel::class.java)
        stateVM.getWordsLD().observe(this, Observer { Int -> onWord(Int) })
        stateVM.getTimeLD().observe(this, Observer { Int -> onTime(Int) })
        Toast.makeText(this, "${stateVM.getCommandMinLD().value}...${stateVM.getCommandMaxLD().value}",
                Toast.LENGTH_LONG).show()
        teamNeed = if (Game.getTeams().size > 0) Game.getTeams().size else Constants.MIN_TEAM_COUNT
        minusButton.setOnClickListener(View.OnClickListener {
            if (teamNeed > stateVM.getCommandMinLD().value!!) {
                teamNeed--
                onCommands(teamNeed)
            }
        })
        plusButton.setOnClickListener(View.OnClickListener {
            if (teamNeed < stateVM.getCommandMaxLD().value!!) {
                teamNeed++
                onCommands(teamNeed)
            }
        })
        onCommands(teamNeed)
        timeMinus.setOnClickListener(View.OnClickListener { stateVM.minusTimeLD() })
        timePlus.setOnClickListener(View.OnClickListener { stateVM.plusTimeLD() })
        wordMinus.setOnClickListener(View.OnClickListener { stateVM.minusWord() })
        wordPlus.setOnClickListener(View.OnClickListener { stateVM.plusWord() })
        val button = findViewById<Button>(R.id.cancel_command)
        button.setOnClickListener(View.OnClickListener {
            stateVM.createTeams(teamNeed)
            startActivity(Intent(this, TeemActivity::class.java))
        })


    }

    private fun onCommands(i: Int?) {
        commands.text = i.toString()
    }

    private fun onTime(i: Int?) {
        time.text = i.toString()
    }

    private fun onWord(i: Int?) {
        words.text = i.toString()
    }
}
