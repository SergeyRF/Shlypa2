package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.game.Dificult
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.viewModel.StateViewModel
import kotlinx.android.synthetic.main.activity_command.*
import java.util.*

class CommandActivity : AppCompatActivity() {

    lateinit var stateVM: StateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_command)

        stateVM = ViewModelProviders.of(this).get(StateViewModel::class.java)

        seekCommand.max = (stateVM.getCommandMaxLD().value!!.toInt() - Constants.MIN_TEAM_COUNT)
        seekCommand.progress = stateVM.getTeemNeed().value!!.toInt() - Constants.MIN_TEAM_COUNT

        seekCommand?.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // Write code to perform some action when progress is changed.
                stateVM.setTeemNeed(seekCommand.progress+Constants.MIN_TEAM_COUNT)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is started.
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is stopped.
            }
        })
        seekTime.max = Constants.MAX_ROUMD_TIME-Constants.MIN_ROUND_TIME
        seekTime.progress = stateVM.getTimeLD().value!!.toInt()-Constants.MIN_ROUND_TIME
        seekTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                stateVM.setTimeLD(seekTime.progress+Constants.MIN_ROUND_TIME)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        seekWord.max = Constants.MAX_WORDS_COUNT-Constants.MIN_WORDS_COUNT
        seekWord.progress = stateVM.getWordsLD().value!!.toInt()-Constants.MIN_WORDS_COUNT
        seekWord.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                stateVM.setWordsLD(seekWord.progress+Constants.MIN_WORDS_COUNT)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        stateVM.getTeemNeed().observe(this, Observer { Int->onCommands(Int) })
        stateVM.getWordsLD().observe(this, Observer { Int -> onWord(Int) })
        stateVM.getTimeLD().observe(this, Observer { Int -> onTime(Int) })
        Toast.makeText(this, "${stateVM.getCommandMinLD().value}...${stateVM.getCommandMaxLD().value}",
                Toast.LENGTH_LONG).show()

        val button = findViewById<Button>(R.id.cancel_command)
        button.setOnClickListener(View.OnClickListener {

            stateVM.createTeams(commands.text.toString().toInt())
            stateVM.onFinish()
            startActivity(Intent(this, TeemActivity::class.java))
        })


        val adapter = ArrayAdapter<Dificult>(this, android.R.layout.simple_list_item_1, Dificult.values())
        spinner.adapter = adapter

    }


    private fun onCommands(i: Int?) {
        commands.text = i.toString()
    }

    private fun onTime(i: Int?) {
        time.text = i.toString()
    }

    private fun onWord(i: Int?) {
        word.text = i.toString()
    }
}
