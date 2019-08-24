package com.example.sergey.shlypa2

import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.crashlytics.android.Crashlytics
import com.example.sergey.shlypa2.di.appModule
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.utils.TimberDebugTree
import com.example.sergey.shlypa2.utils.TimberReleaseTree
import com.flurry.android.FlurryAgent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by alex on 4/4/18.
 */
class App : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(appModule))
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        } else {
            Timber.plant(TimberReleaseTree())
        }

        //todo refactor this shit  !!!
        val namesArray = resources.getStringArray(R.array.teams)
        Game.teamNames = namesArray.toMutableList()

        buildCaoc()

        Fabric.with(this, Crashlytics())
        FirebaseAnalytics.getInstance(this)
                .setAnalyticsCollectionEnabled(BuildConfig.DEBUG.not())

        FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "S85BYYPTT7FXNJZCTPB3")

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
        // todo review this strange error - IllegalStateException
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(
                    if (BuildConfig.DEBUG) Constants.DEBUG_COMMON_TOPIC
                    else Constants.RELEASE_COMMON_TOPIC
            )
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }
}