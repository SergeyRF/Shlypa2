package com.example.sergey.shlypa2.beans

import android.arch.persistence.room.*

/**
 * Created by alex on 4/10/18.
 */
@Entity(tableName = Contract.PLAYER_TABLE,
        indices = [Index(value = Contract.PLAYER_NAME, unique = true)])
class Player(@ColumnInfo(name = Contract.PLAYER_NAME) var name: String = "Nameless",
             @PrimaryKey(autoGenerate = true) @ColumnInfo(name = Contract.PLAYER_ID) var id : Long = 0,
             @ColumnInfo(name = Contract.PLAYER_TYPE) var type: Contract.PlayerType = Contract.PlayerType.USER
             ) {

    override fun toString(): String {
        return "Player $name id $id "
    }

    override fun equals(other: Any?): Boolean {
        if(other is Player) {
            return other.id == id
        } else return false
    }
}