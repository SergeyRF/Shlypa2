package com.example.sergey.shlypa2.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sergey.shlypa2.beans.Lang
import com.example.sergey.shlypa2.beans.Type

@Dao
interface TypesDao {

    @Insert
    fun insertType(type: Type): Long

    @Query("SELECT * FROM ${Contract.TYPES_TABLE}")
    fun getAllTypes(): List<Type>

    @Query("SELECT * FROM ${Contract.TYPES_TABLE} WHERE ${Contract.TYPE_LANG} = :lang")
    fun getTypesForLang(lang: Lang): List<Type>
}