package com.example.sergey.shlypa2.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.sergey.shlypa2.beans.Contract
import com.example.sergey.shlypa2.beans.Player

/**
 * Created by alex on 4/10/18.
 */
@Dao
interface PlayersDao {

    @Query("SELECT * FROM ${Contract.PLAYER_TABLE} ORDER BY RANDOM()")
    fun getAllPlayers() : List<Player>

    @Query("SELECT * FROM ${Contract.PLAYER_TABLE} WHERE ${Contract.PLAYER_ID} = :id")
    fun getPlayerById(id : Long) : Player?

    @Query("SELECT * FROM ${Contract.PLAYER_TABLE} WHERE ${Contract.PLAYER_NAME} = :name")
    fun getPlayerByName(name : String) : Player?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlayer(player: Player) : Long
}