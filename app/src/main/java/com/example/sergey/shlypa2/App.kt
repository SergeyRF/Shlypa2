package com.example.sergey.shlypa2

import android.app.Application
import com.example.sergey.shlypa2.db.DataBase
import com.example.sergey.shlypa2.db.DbCreator
import timber.log.Timber

/**
 * Created by alex on 4/4/18.
 */
class App  : Application(){

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        DbCreator.createPlayers(DataBase.getInstance(this))
        DbCreator.createWords(DataBase.getInstance(this), this)
        DbCreator.loadFileList(this)
    }
}