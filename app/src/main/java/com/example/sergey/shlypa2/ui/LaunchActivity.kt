package com.example.sergey.shlypa2.ui

/**
 * Created by sergey on 4/25/18.
 */

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.appodeal.ads.Appodeal
import com.appodeal.ads.Native
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.debug


class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setTheme(this)
        super.onCreate(savedInstanceState)

        initAppodeal()

        startActivity(Intent(this, FirstActivity::class.java))
        finish()
    }

    fun initAppodeal() {
        val appKey = ""
        debug { Appodeal.setTesting(true) }
        Appodeal.disableLocationPermissionCheck()
        Appodeal.disableWriteExternalStoragePermissionCheck()
        Appodeal.setAutoCache(Appodeal.NATIVE, false)
        Appodeal.initialize(this, appKey, Appodeal.NATIVE)
        Appodeal.setNativeAdType(Native.NativeAdType.Auto)
        Appodeal.cache(this, Appodeal.NATIVE, 1)
    }
}