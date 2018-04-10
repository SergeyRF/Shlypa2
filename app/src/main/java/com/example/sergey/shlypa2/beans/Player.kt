package com.example.sergey.shlypa2.beans

import android.arch.persistence.room.*

/**
 * Created by alex on 4/10/18.
 */
@Entity(tableName = Contract.PLAYER_TABLE,
        indices = [Index(value = Contract.PLAYER_NAME, unique = true)])
class Player(@ColumnInfo(name = Contract.PLAYER_NAME) var name: String = "Nameless",
             @Ignore var words: MutableList<String>? = null,
             @Ignore var scores: Int = 0,
             @PrimaryKey(autoGenerate = true) @ColumnInfo(name = Contract.PLAYER_ID) var id : Int = 0) {

    override fun toString(): String {
        return "Player $name id $id scores $scores"
    }
}