package com.example.sergey.shlypa2.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.StateRepresent
import com.example.sergey.shlypa2.beans.Type
import com.example.sergey.shlypa2.beans.Word

/**
 * Created by alex on 4/10/18.
 */

@Database(entities = arrayOf(Player::class, Word::class, StateRepresent::class, Type::class), version = Contract.DB_VERSION)
@TypeConverters(WordTypeConverter::class, PlayerTypeConverter::class, LangsConverter::class)
abstract class DataBase : RoomDatabase() {

    abstract fun playersDao(): PlayersDao
    abstract fun wordDao(): WordsDao
    abstract fun stateDao(): StateDao
    abstract fun typesDap(): TypesDao

}