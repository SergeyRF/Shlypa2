package com.example.sergey.shlypa2.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.sergey.shlypa2.beans.Contract
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
}