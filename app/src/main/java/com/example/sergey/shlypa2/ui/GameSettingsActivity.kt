package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.game.WordType
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


        settingsVM.getWordsLD().observe(this, Observer { Int -> onWord(Int) })
        settingsVM.getTimeLD().observe(this, Observer { Int -> onTime(Int) })
        settingsVM.getnumberMinusBal().observe(this, Observer { Int -> onNumberMinusBal(Int) })

        onSwitch(settingsVM.getAllowRandom().value!!)
        onBalSwitch(settingsVM.getMinusBal().value!!)


        val button = findViewById<Button>(R.id.bt_cancel_command)
        button.setOnClickListener{

            settingsVM.setAllowRandom(cbAllowRandom.isChecked)
            settingsVM.setDificultLD(spinnerDificult.selectedItem as WordType)
            settingsVM.setnumberMInusBal(tvNumberMinusBal.text.toString().toInt())
            settingsVM.setMinusBal(cbMinusBal.isChecked)
            settingsVM.onFinish()
            startActivity(Intent(this, WordsInActivity::class.java))
        }


        val typesList = arrayOf(WordType.EASY, WordType.MEDIUM, WordType.HARD, WordType.VERY_HARD)
        val adapter = ArrayAdapter<WordType>(this, android.R.layout.simple_list_item_1, typesList)
        spinnerDificult.adapter = adapter

        onDificult(settingsVM.getDificultLD().value)
    }


    private fun onTime(i: Int?) {
        time.text = i.toString()
    }

    private fun onWord(i: Int?) {
        word.text = i.toString()
    }

    private fun onDificult(d:WordType?){
        Timber.d("Difficulty $d position ${WordType.values().indexOf(d)}")
        spinnerDificult.setSelection(WordType.values().indexOf(d))
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
