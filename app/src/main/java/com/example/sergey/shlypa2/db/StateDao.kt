package com.example.sergey.shlypa2.db

import androidx.room.*
import com.example.sergey.shlypa2.beans.StateRepresent

/**
 * Created by alex on 4/17/18.
 */

@Dao
interface StateDao {
    @Query("SELECT * FROM ${Contract.STATE_TABLE}")
    fun getAllStates() : List<StateRepresent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertState(stateRepresent: StateRepresent)

    @Query("DELETE FROM ${Contract.STATE_TABLE} WHERE ${Contract.STATE_GAME_ID} = :gameId")
    fun deleteState(gameId : Int)
}