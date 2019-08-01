package com.example.sergey.shlypa2.screens.splash

/**
 * Created by sergey on 4/25/18.
 */

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.main.FirstActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class LaunchActivity : AppCompatActivity() {

    companion object {
        private const val STORAGE_REQUEST = 1056
    }

    val viewModel by viewModel<LaunchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)

        viewModel.startMainLD.observeSafe(this) {
            startActivity(Intent(this@LaunchActivity, FirstActivity::class.java))
            finish()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == STORAGE_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //exportDb()
        }
    }
}