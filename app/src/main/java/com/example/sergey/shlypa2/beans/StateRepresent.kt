package com.example.sergey.shlypa2.beans

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sergey.shlypa2.db.Contract

/**
 * Created by alex on 4/17/18.
 */

@Entity(tableName = Contract.STATE_TABLE,
        indices = [Index(value = arrayOf(Contract.STATE_GAME_ID), unique = true)])
class StateRepresent(@PrimaryKey(autoGenerate = true)
                     @ColumnInfo(name = Contract.STATE_ID) var id: Long,
                     @ColumnInfo(name = Contract.STATE_GAME_ID) var gameId: Int,
                     @ColumnInfo(name = Contract.STATE_TIME) var time: Long,
                     @ColumnInfo(name = Contract.STATE_STRING) var state: String)