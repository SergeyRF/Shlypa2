package com.example.sergey.shlypa2.beans

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sergey.shlypa2.db.Contract

@Entity(tableName = Contract.TYPES_TABLE)
class Type (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = Contract.TYPE_ID)
        var id: Long = 0,
        @ColumnInfo(name = Contract.TYPE_NAME)
        var name: String,
        @ColumnInfo(name = Contract.TYPE_LANG)
        var lang: Lang
        )