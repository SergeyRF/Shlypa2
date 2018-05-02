package com.example.sergey.shlypa2.ui

/**
 * Created by sergey on 4/25/18.
 */

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.Functions


class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setTheme(this)
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, FirstActivity::class.java))
        finish()
    }

}