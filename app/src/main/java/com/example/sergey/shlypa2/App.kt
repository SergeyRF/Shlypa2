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
import com.crashlytics.android.Crashlytics
import com.example.sergey.shlypa2.db.DbCreator
import com.example.sergey.shlypa2.screens.splash.LaunchActivity
import com.example.sergey.shlypa2.utils.DbExporter
import com.example.sergey.shlypa2.utils.PreferenceHelper
import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric
import com.flurry.android.FlurryAgent

/**
 * Created by alex on 4/4/18.
 */
class App : MultiDexApplication() {

    companion object {
        private const val DB_IMPORTED = "db_imported_v1_1_4d"
    }

    override fun onCreate() {
        super.onCreate()

        manageDb()

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
        try{
            FirebaseMessaging.getInstance().subscribeToTopic(
                    if (BuildConfig.DEBUG) Constants.DEBUG_COMMON_TOPIC
                    else Constants.RELEASE_COMMON_TOPIC
            )
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    private fun manageDb() {
        val preferences = PreferenceHelper.defaultPrefs(this)
        val dbImported = preferences.getBoolean(DB_IMPORTED, false)
        if (!dbImported) {
            val success = DbExporter().importDbFromAsset(this, "shlyapa_db")
            preferences[DB_IMPORTED] = success
        } else {
            Timber.d("Db already imported")
        }
    }
}