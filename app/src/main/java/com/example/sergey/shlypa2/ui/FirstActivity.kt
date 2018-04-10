package com.example.sergey.shlypa2.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.db.DataBase
import timber.log.Timber

class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        val button1: Button= findViewById(R.id.play)

        button1.setOnClickListener(View.OnClickListener {
            val intent:Intent=Intent(this, PlayersActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
        })

        val db = DataBase.getInstance(applicationContext)
        db.playersDao().insertPlayer(Player(name = "Vasya"))
        val players = db.playersDao().getAllPlayers()
        for(player in players) {
            Timber.d("player $player")
        }
    }
}
