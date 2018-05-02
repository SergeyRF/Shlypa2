package com.example.sergey.shlypa2

import android.app.Application
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.utils.DbExporter
import com.example.sergey.shlypa2.utils.PreferenceHelper
import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import com.example.sergey.shlypa2.utils.TimberTree
import timber.log.Timber

/**
 * Created by alex on 4/4/18.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(TimberTree())

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
    }

    companion object {
        private const val DB_IMPORTED = "db_imported"
    }
}