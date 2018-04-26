package com.example.sergey.shlypa2.ui

/**
 * Created by sergey on 4/25/18.
 */

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, FirstActivity::class.java))
        finish()
    }

}