package com.example.sergey.shlypa2

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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
        stateVM.getCommandLD().observe(this, Observer { Int-> onCommands(Int) });

         Toast.makeText(this, "${stateVM.getCommandMinLD().value}...${stateVM.getCommandMaxLD().value}",
                 Toast.LENGTH_LONG).show()
         minusButton.setOnClickListener(View.OnClickListener { stateVM.getMinusCommLD()      })
         plusButton.setOnClickListener(View.OnClickListener { stateVM.getPlusCommLD() })
        val button =findViewById<Button>(R.id.cancel_command)
        button.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, TeemActivity::class.java))
        })
    }

    private fun onCommands(i:Int?){ commands.text = i.toString()}
}
