package com.example.sergey.shlypa2.beans

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sergey.shlypa2.db.Contract
import com.example.sergey.shlypa2.game.PlayerType

/**
 * Created by alex on 4/10/18.
 */
@Entity(tableName = Contract.PLAYER_TABLE,
        indices = [Index(value = arrayOf(Contract.PLAYER_NAME), unique = true)])
class Player(@ColumnInfo(name = Contract.PLAYER_NAME) var name: String = "Nameless",
             @ColumnInfo(name = Contract.PLAYER_LOCALE) var locale: String = "en",
             @PrimaryKey(autoGenerate = true) @ColumnInfo(name = Contract.PLAYER_ID) var id: Long = 0,
             @ColumnInfo(name = Contract.PLAYER_AVATAR) var avatar: String = "",
             @ColumnInfo(name = Contract.PLAYER_TYPE) var type: PlayerType = PlayerType.USER
) {

    override fun toString(): String {
        return "Player $name id $id "
    }

    override fun equals(other: Any?): Boolean {
        if (other is Player) {
            return other.id == id
        } else return false
    }
}