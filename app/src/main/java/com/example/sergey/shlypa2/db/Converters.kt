package com.example.sergey.shlypa2.db

import android.arch.persistence.room.TypeConverter
import com.example.sergey.shlypa2.beans.Contract

/**
 * Created by alex on 4/15/18.
 */

class WordTypeConverter {


    @TypeConverter
    fun typeToString(type: Contract.WordType): String {
        return type.toString()
    }

    @TypeConverter
    fun toType(type: String): Contract.WordType {
        return when (type) {
            Contract.WordType.EASY.toString() -> Contract.WordType.EASY
            Contract.WordType.MEDIUM.toString() -> Contract.WordType.MEDIUM
            Contract.WordType.HARD.toString() -> Contract.WordType.HARD
            Contract.WordType.VERY_HARD.toString() -> Contract.WordType.VERY_HARD
            Contract.WordType.USER.toString() -> Contract.WordType.USER
            else -> throw RuntimeException("Unsupported type")
        }
    }

}