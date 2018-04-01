package com.example.sergey.shlypa2

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_command.*

class CommandActivity : AppCompatActivity() {

    lateinit var stateVM :StateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_command)

        val minusButton = findViewById<Button>(R.id.minusCommand)
        val plusButton = findViewById<Button>(R.id.plusCommand)
        val commands = findViewById<TextView>(R.id.commands)
        stateVM = ViewModelProviders.of(this).get(StateViewModel::class.java)
       // stateVM.getCommandLD().observe(this, Observer { Int-> onCommands(Int) });
        //stateVM.setCommandLD(stateVM.getCommandMaxLD().value!!.toInt())
        stateVM.updateStateData()
         Toast.makeText(this, "${stateVM.getCommandMinlLD().value}...${stateVM.getCommandMaxLD().value}",
                 Toast.LENGTH_LONG).show()
         minusButton.setOnClickListener(View.OnClickListener {
             if (commands.text.toString().toInt() == stateVM.getCommandMinlLD().value){
                 Toast.makeText(this,"No",Toast.LENGTH_LONG).show()
             }
             else stateVM.setCommandLD(stateVM.getCommandLD().value!!.toInt()-1)
         })
    }

    private fun onCommands(i:Int?){ commands.text = i.toString()}
}
