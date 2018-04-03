package com.example.sergey.shlypa2.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.viewModel.RoundViewModel

class RoundActivity : AppCompatActivity() {

    lateinit var viewModel : RoundViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RoundViewModel::class.java)

        startStartFragment()
    }

    fun startStartFragment() {
        val fragment = RoundStartFragment()
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit()
    }
}
