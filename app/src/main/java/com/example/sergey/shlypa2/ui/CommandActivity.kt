package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.game.Dificult
import com.example.sergey.shlypa2.viewModel.StateViewModel
import kotlinx.android.synthetic.main.activity_game_settings.*
import com.example.sergey.shlypa2.utils.onChange
import timber.log.Timber


class CommandActivity : AppCompatActivity() {

    lateinit var stateVM: StateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        stateVM = ViewModelProviders.of(this).get(StateViewModel::class.java)

        seekCommand.max = (stateVM.getCommandMaxLD().value!!.toInt() - Constants.MIN_TEAM_COUNT)
        seekCommand.progress = stateVM.getTeemNeed().value!!.toInt() - Constants.MIN_TEAM_COUNT
        seekCommand?.onChange{_, _, _ ->
            stateVM.setTeemNeed(seekCommand.progress+Constants.MIN_TEAM_COUNT)
        }

        seekTime.max = Constants.MAX_ROUMD_TIME-Constants.MIN_ROUND_TIME
        seekTime.progress = stateVM.getTimeLD().value!!.toInt()-Constants.MIN_ROUND_TIME
        seekTime.onChange { _, _, _ ->
            stateVM.setTimeLD(seekTime.progress+Constants.MIN_ROUND_TIME)}

        seekWord.max = Constants.MAX_WORDS_COUNT-Constants.MIN_WORDS_COUNT
        seekWord.progress = stateVM.getWordsLD().value!!.toInt()-Constants.MIN_WORDS_COUNT
        seekWord.onChange{ _, _, _ ->
            stateVM.setWordsLD(seekWord.progress+Constants.MIN_WORDS_COUNT)
        }

        stateVM.getTeemNeed().observe(this, Observer { Int->onCommands(Int) })
        stateVM.getWordsLD().observe(this, Observer { Int -> onWord(Int) })
        stateVM.getTimeLD().observe(this, Observer { Int -> onTime(Int) })



        onCheckBox(stateVM.getAutoAddWord().value!!)


        val button = findViewById<Button>(R.id.cancel_command)
        button.setOnClickListener(View.OnClickListener {

            stateVM.createTeams(commands.text.toString().toInt())
            stateVM.setAutoAddWord(cbAddAutoWord.isChecked)
            stateVM.setDificultLD(spinnerDificult.selectedItem as Dificult)
            stateVM.onFinish()
            startActivity(Intent(this, TeemActivity::class.java))
        })


        val adapter = ArrayAdapter<Dificult>(this, android.R.layout.simple_list_item_1, Dificult.values())
        spinnerDificult.adapter = adapter

        onDificult(stateVM.getDificultLD().value)
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
    private fun onDificult(d:Dificult?){
        Timber.d("Difficulty $d position ${Dificult.values().indexOf(d)}")
        spinnerDificult.setSelection(Dificult.values().indexOf(d))
    }
    private fun onCheckBox(b:Boolean){
        Timber.d("$b")
        cbAddAutoWord.isChecked = b
    }
}
