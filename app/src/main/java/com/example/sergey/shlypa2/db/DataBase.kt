package com.example.sergey.shlypa2.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.example.sergey.shlypa2.beans.Contract
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Word

/**
 * Created by alex on 4/10/18.
 */

@Database(entities = arrayOf(Player::class, Word::class), version = Contract.DB_VERSION)
@TypeConverters(WordTypeConverter::class, PlayerTypeConverter::class)
abstract class DataBase : RoomDatabase(){

    abstract fun playersDao() : PlayersDao
    abstract fun wordDao() : WordsDao

    companion object {
        private var INSTANCE : DataBase? = null

        fun getInstance(context : Context) : DataBase {
            if(INSTANCE == null) {
                synchronized(DataBase::class) {
                    INSTANCE = Room.databaseBuilder(context, DataBase::class.java, Contract.DB_NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build()
                }
            }

            return INSTANCE!!
        }
    }
}