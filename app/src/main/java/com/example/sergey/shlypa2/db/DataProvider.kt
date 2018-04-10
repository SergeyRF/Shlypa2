package com.example.sergey.shlypa2.db

import android.content.Context
import com.example.sergey.shlypa2.beans.Player

/**
 * Created by alex on 4/10/18.
 */
class DataProvider(val context: Context) {
    val db = DataBase.getInstance(context)

    fun getPlayers() : List<Player> = db.playersDao().getAllPlayers()

    fun getPlayer(id : Long) = db.playersDao().getPlayerById(id)

    fun insertPlayer(player : Player) : Long {
        return db.playersDao().insertPlayer(player)
    }
}