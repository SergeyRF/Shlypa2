package com.example.sergey.shlypa2

import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.example.sergey.shlypa2.ads.AdsManager
import com.example.sergey.shlypa2.di.appModule
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.utils.TimberDebugTree
import com.example.sergey.shlypa2.utils.TimberReleaseTree
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.android.startKoin
import timber.log.Timber


/**
 * Created by alex on 4/4/18.
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))

        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        } else {
            Timber.plant(TimberReleaseTree())
        }

        AdsManager.initAds(this)
        //todo refactor this shit  !!!
        val namesArray = resources.getStringArray(R.array.teams)
        Game.teamNames = namesArray.toMutableList()

        buildCaoc()
        initFcm()
    }

    fun buildCaoc() {
        //Catch fatal exceptions
        CaocConfig.Builder.create()
                .logErrorOnRestart(true)
                .showErrorDetails(BuildConfig.DEBUG)
                .apply()
    }

    private fun initFcm() {
        FirebaseMessaging.getInstance().subscribeToTopic(
                if (BuildConfig.DEBUG) Constants.DEBUG_COMMON_TOPIC
                else Constants.RELEASE_COMMON_TOPIC
        )
    }
}