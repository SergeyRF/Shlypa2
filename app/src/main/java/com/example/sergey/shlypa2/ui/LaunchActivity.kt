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
        Appodeal.setTesting(true)
        Appodeal.disableLocationPermissionCheck()
        Appodeal.initialize(this, appKey, Appodeal.NATIVE)
        Appodeal.cache(this, Appodeal.NATIVE)
        Appodeal.setNativeAdType(Native.NativeAdType.Auto)
    }
}