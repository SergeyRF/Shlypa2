package com.example.sergey.shlypa2.beans

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by alex on 4/17/18.
 */

@Entity(tableName = Contract.STATE_TABLE)
class StateRepresent(@PrimaryKey(autoGenerate = true)
                     @ColumnInfo(name = Contract.STATE_ID) var id : Long,
                     @ColumnInfo(name = Contract.STATE_TIME) var time : Long,
                     @ColumnInfo(name = Contract.STATE_STRING) var state : String)