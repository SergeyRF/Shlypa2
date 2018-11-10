package com.example.sergey.shlypa2

import android.support.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.example.sergey.shlypa2.ads.AdsManager
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.utils.DbExporter
import com.example.sergey.shlypa2.utils.PreferenceHelper
import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import com.example.sergey.shlypa2.utils.TimberDebugTree
import com.example.sergey.shlypa2.utils.TimberReleaseTree
import timber.log.Timber


/**
 * Created by alex on 4/4/18.
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        } else {
            Timber.plant(TimberReleaseTree())
        }

        AdsManager.initAds(this)
        val namesArray = resources.getStringArray(R.array.teams)
        Game.teamNames = namesArray.toMutableList()

//        DbCreator.createPlayers(DataBase.getInstance(this), this)
//        DbCreator.createWords(DataBase.getInstance(this), this)
//        DbCreator.sortAndWritesWordsFromRes(this)
//        DbCreator.sortAndWriteWords(this)
//        DbCreator.loadFileList(this)
//        DbExporter().exportDbToFile(this, Contract.DB_NAME)

        val preferences = PreferenceHelper.defaultPrefs(this)
        val dbImported = preferences.getBoolean(DB_IMPORTED, false)
        if (!dbImported) {
            val success = DbExporter().importDbFromAsset(this, "shlyapa_db")
            preferences[DB_IMPORTED] = success
        } else {
            Timber.d("Db already imported")
        }

        buildCaoc()
    }

    fun buildCaoc() {
        //Catch fatal exceptions
        CaocConfig.Builder.create()
                .logErrorOnRestart(true)
                .showErrorDetails(BuildConfig.DEBUG)
                .apply()
    }

    companion object {
        private const val DB_IMPORTED = "db_imported"
    }
}