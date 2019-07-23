package com.example.sergey.shlypa2.screens.splash

/**
 * Created by sergey on 4/25/18.
 */

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sergey.shlypa2.BuildConfig
import com.example.sergey.shlypa2.db.Contract
import com.example.sergey.shlypa2.db.DataBase
import com.example.sergey.shlypa2.db.DbCreator
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.main.FirstActivity
import com.example.sergey.shlypa2.utils.DbExporter
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.PreferenceHelper
import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class LaunchActivity : AppCompatActivity() {

    companion object {
        private const val STORAGE_REQUEST = 1056
    }

    val viewModel by viewModel<LaunchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)

        /*GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val db = DataBase.getInstance(this@LaunchActivity)
                DbCreator.createPlayers(db, this@LaunchActivity)
                DbCreator.createWords(db, this@LaunchActivity)
            }

            startActivity(Intent(this@LaunchActivity, FirstActivity::class.java))
            finish()
        }*/



        /*if(BuildConfig.DEBUG) {
            exportDb()
        }*/

        viewModel.startMainLD.observeSafe(this) {
            startActivity(Intent(this@LaunchActivity, FirstActivity::class.java))
            finish()
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == STORAGE_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //exportDb()
        }
    }
}