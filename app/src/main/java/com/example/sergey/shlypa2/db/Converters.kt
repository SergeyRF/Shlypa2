package com.example.sergey.shlypa2.db

import androidx.room.TypeConverter
import com.example.sergey.shlypa2.beans.AvatarType
import com.example.sergey.shlypa2.beans.Lang
import com.example.sergey.shlypa2.beans.PlayerType
import com.example.sergey.shlypa2.beans.WordType


/**
 * Created by alex on 4/15/18.
 */

class WordTypeConverter {

    @TypeConverter
    fun typeToString(type: WordType): String {
        return type.toString()
    }

    @TypeConverter
    fun toType(type: String): WordType {
        return when (type) {
            WordType.EASY.toString() -> WordType.EASY
            WordType.MEDIUM.toString() -> WordType.MEDIUM
            WordType.HARD.toString() -> WordType.HARD
            WordType.VERY_HARD.toString() -> WordType.VERY_HARD
            WordType.USER.toString() -> WordType.USER
            else -> throw RuntimeException("Unsupported type")
        }
    }

}

class PlayerTypeConverter {

    @TypeConverter
    fun typeToString(type: PlayerType): String {
        return type.toString()
    }

    @TypeConverter
    fun stringToType(type: String): PlayerType {
        return when (type) {
            PlayerType.USER.toString() -> PlayerType.USER
            PlayerType.STANDARD.toString() -> PlayerType.STANDARD
            else -> throw RuntimeException("Unsupported type")
        }
    }
}

class LangsConverter {
    @TypeConverter
    fun langToString(lang: Lang) = lang.toString()

    @TypeConverter
    fun stringToLang(lang: String) = Lang.valueOf(lang)
}

class AvatarTypeConverter {

    @TypeConverter
    fun avatarTypeToString(avatarType: AvatarType):String{
        return avatarType.toString()
    }

    @TypeConverter
    fun stringToAvatarType(avatarType: String): AvatarType {
        return when(avatarType){
            AvatarType.USER.toString() -> AvatarType.USER
            AvatarType.STANDARD.toString() -> AvatarType.STANDARD
            else -> throw RuntimeException("Unsupported type")
        }
    }
}