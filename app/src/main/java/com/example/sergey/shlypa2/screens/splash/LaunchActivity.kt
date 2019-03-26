package com.example.sergey.shlypa2.screens.splash

/**
 * Created by sergey on 4/25/18.
 */

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sergey.shlypa2.db.DataBase
import com.example.sergey.shlypa2.db.DbCreator
import com.example.sergey.shlypa2.screens.main.FirstActivity
import com.example.sergey.shlypa2.utils.Functions
import kotlinx.coroutines.*


class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setThemeApi21(this)
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val db = DataBase.getInstance(this@LaunchActivity)
                DbCreator.createPlayers(db, this@LaunchActivity)
                DbCreator.createWords(db, this@LaunchActivity)
            }

            startActivity(Intent(this@LaunchActivity, FirstActivity::class.java))
            finish()
        }
    }

}