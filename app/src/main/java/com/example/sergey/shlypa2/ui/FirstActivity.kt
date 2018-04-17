package com.example.sergey.shlypa2.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ui.fragments.LoadStateFragment
import com.example.sergey.shlypa2.ui.fragments.WelcomeFragment
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

    }

    override fun onStart() {
        super.onStart()
        val fragment = WelcomeFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerFirst, fragment)
                .commit()
    }

    fun startGameLoadFragment() {
        val fragment = LoadStateFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerFirst, fragment)
                .addToBackStack(null)
                .commit()
    }
}
