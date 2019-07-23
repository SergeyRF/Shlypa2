package com.example.sergey.shlypa2.db

import androidx.room.*
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.game.PlayerType

/**
 * Created by alex on 4/10/18.
 */
@Dao
interface PlayersDao {

    @Query("SELECT * FROM ${Contract.PLAYER_TABLE} " +
            "WHERE ${Contract.PLAYER_LOCALE} =:locale " +
            "ORDER BY RANDOM()")
    fun getAllPlayers(locale : String = "en") : List<Player>

    @Query("SELECT * FROM ${Contract.PLAYER_TABLE} " +
            "WHERE ${Contract.PLAYER_TYPE}= :type " +
            "AND ${Contract.PLAYER_LOCALE}= :locale " +
            "ORDER BY RANDOM()")
    fun getPlayersByType(type : PlayerType, locale: String = "en") : List<Player>

    @Query("SELECT * FROM ${Contract.PLAYER_TABLE} WHERE ${Contract.PLAYER_ID} = :id")
    fun getPlayerById(id : Long) : Player?

    @Query("SELECT * FROM ${Contract.PLAYER_TABLE} WHERE ${Contract.PLAYER_NAME} = :name")
    fun getPlayerByName(name : String) : Player?

    @Query("SELECT * FROM ${Contract.PLAYER_TABLE} " +
            "WHERE ${Contract.PLAYER_TYPE}= :type")
    fun getPlayersByType(type : PlayerType) : List<Player>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Player) : Long

    @Delete
    fun deletePlayer(players: List<Player>):Int

    @Delete
    fun deletePlayer(player: Player):Int
}