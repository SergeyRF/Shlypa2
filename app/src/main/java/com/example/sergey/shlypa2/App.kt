package com.example.sergey.shlypa2

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.example.sergey.shlypa2.data.AppLifecycleObserver
import com.example.sergey.shlypa2.di.appModule
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.utils.TimberDebugTree
import com.example.sergey.shlypa2.utils.TimberReleaseTree
import com.flurry.android.FlurryAgent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by alex on 4/4/18.
 */
class App : Application() {

    private val lifecycleObserver by inject<AppLifecycleObserver>()

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

        buildCaoc()

        FirebaseAnalytics.getInstance(this)
                .setAnalyticsCollectionEnabled(BuildConfig.DEBUG.not())

        FlurryAgent.Builder()
                .withLogEnabled(BuildConfig.DEBUG.not())
                .build(this, "S85BYYPTT7FXNJZCTPB3")

        tryToInit()
    }

    fun buildCaoc() {
        //Catch fatal exceptions
        CaocConfig.Builder.create()
                .logErrorOnRestart(true)
                .showErrorDetails(BuildConfig.DEBUG)
                .apply()
    }

    private fun tryToInit() {
        // will throw an exception in a CustomActivityOnCrash
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(
                    if (BuildConfig.DEBUG) Constants.DEBUG_COMMON_TOPIC
                    else Constants.RELEASE_COMMON_TOPIC
            )
        } catch (ex: Exception) {
            Timber.e(ex)
        }

        try {
            ProcessLifecycleOwner.get().lifecycle
                    .addObserver(lifecycleObserver)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }
}