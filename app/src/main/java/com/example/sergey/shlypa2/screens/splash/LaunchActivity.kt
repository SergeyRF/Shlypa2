package com.example.sergey.shlypa2.screens.splash

/**
 * Created by sergey on 4/25/18.
 */

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sergey.shlypa2.screens.FirstActivity
import com.example.sergey.shlypa2.utils.Functions


class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setThemeApi21(this)
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, FirstActivity::class.java))
        finish()
    }

}