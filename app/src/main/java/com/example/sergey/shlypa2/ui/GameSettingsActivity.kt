package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.game.Dificult
import com.example.sergey.shlypa2.utils.hide
import com.example.sergey.shlypa2.viewModel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_game_settings.*
import com.example.sergey.shlypa2.utils.onChange
import timber.log.Timber


class GameSettingsActivity : AppCompatActivity() {

    lateinit var settingsVM: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)


        settingsVM = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        if (settingsVM.getCommandMaxLD().value!!.toInt()==Constants.MIN_TEAM_COUNT){
            seekCommand.hide()
            stateTeems.setOnClickListener {
                Toast.makeText(this,"При таком колличестве игроков " +
                        "\nкомманд может быть только две",
                        Toast.LENGTH_LONG).show()
            }
        }
        seekCommand.max = (settingsVM.getCommandMaxLD().value!!.toInt() - Constants.MIN_TEAM_COUNT)
        seekCommand.progress = settingsVM.getTeemNeed().value!!.toInt() - Constants.MIN_TEAM_COUNT
        seekCommand?.onChange{_, _, _ ->
            settingsVM.setTeemNeed(seekCommand.progress+Constants.MIN_TEAM_COUNT)
        }

        seekTime.max = Constants.MAX_ROUMD_TIME-Constants.MIN_ROUND_TIME
        seekTime.progress = settingsVM.getTimeLD().value!!.toInt()-Constants.MIN_ROUND_TIME
        seekTime.onChange { _, _, _ ->
            settingsVM.setTimeLD(seekTime.progress+Constants.MIN_ROUND_TIME)
        }

        seekWord.max = Constants.MAX_WORDS_COUNT-Constants.MIN_WORDS_COUNT
        seekWord.progress = settingsVM.getWordsLD().value!!.toInt()-Constants.MIN_WORDS_COUNT
        seekWord.onChange{ _, _, _ ->
            settingsVM.setWordsLD(seekWord.progress+Constants.MIN_WORDS_COUNT)
        }

        seekMinusBal.max = Constants.MAX_MINUS_BAL - Constants.MIN_MINUS_BAL
        seekMinusBal.progress = settingsVM.getnumberMinusBal().value!!.toInt() - Constants.MIN_MINUS_BAL
        seekMinusBal.onChange { _, _, _ ->
            settingsVM.setnumberMInusBal(seekMinusBal.progress + Constants.MIN_MINUS_BAL)
        }


        settingsVM.getTeemNeed().observe(this, Observer { Int->onCommands(Int) })
        settingsVM.getWordsLD().observe(this, Observer { Int -> onWord(Int) })
        settingsVM.getTimeLD().observe(this, Observer { Int -> onTime(Int) })
        settingsVM.getnumberMinusBal().observe(this, Observer { Int -> onNumberMinusBal(Int) })

        onSwitch(settingsVM.getAllowRandom().value!!)
        onBalSwitch(settingsVM.getMinusBal().value!!)


        val button = findViewById<Button>(R.id.cancel_command)
        button.setOnClickListener{

            settingsVM.createTeams(teemSize.text.toString().toInt())
            settingsVM.setAllowRandom(cbAllowRandom.isChecked)
            settingsVM.setDificultLD(spinnerDificult.selectedItem as Dificult)
            settingsVM.setnumberMInusBal(tvNumberMinusBal.text.toString().toInt())
            settingsVM.setMinusBal(cbMinusBal.isChecked)
            settingsVM.onFinish()
            startActivity(Intent(this, TeemActivity::class.java))
        }


        val adapter = ArrayAdapter<Dificult>(this, android.R.layout.simple_list_item_1, Dificult.values())
        spinnerDificult.adapter = adapter

        onDificult(settingsVM.getDificultLD().value)
    }


    private fun onCommands(i: Int?) {
        teemSize.text = i.toString()
        Timber.d("inject ${i.toString()} ")
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

    private fun onSwitch(b:Boolean){
        Timber.d("$b")
        cbAllowRandom.isChecked = b
    }

    private fun onBalSwitch(b:Boolean?){
        cbMinusBal.isChecked = b!!
    }

    private fun onNumberMinusBal(i:Int?){
        tvNumberMinusBal.text = i.toString()
    }
}
