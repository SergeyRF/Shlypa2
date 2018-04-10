package com.example.sergey.shlypa2.beans

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by alex on 4/10/18.
 */

@Entity(tableName = Contract.WORD_TABLE)
class Word(@ColumnInfo(name = Contract.WORD_COLUMN) var word: String = "",
           @PrimaryKey @ColumnInfo(name = Contract.WORD_ID) var id: Int = -1,
           @ColumnInfo(name = Contract.WORD_LANG) var lang: String = "RU",
           @Ignore var addedBy: Int = -1) {
    @Ignore
    var play: Boolean = false
    @Ignore
    var right: Boolean = false

    @Ignore
    var answeredBy = -1
}