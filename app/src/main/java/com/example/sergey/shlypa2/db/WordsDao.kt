package com.example.sergey.shlypa2.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.sergey.shlypa2.beans.Contract
import com.example.sergey.shlypa2.beans.Word

/**
 * Created by alex on 4/10/18.
 */

@Dao
interface WordsDao {

    @Query("SELECT * FROM ${Contract.WORD_TABLE}")
    fun getAllWords() : List<Word>

    @Query("SELECT * FROM ${Contract.WORD_TABLE} WHERE ${Contract.WORD_ID} = :id")
    fun getWordById(id : Long) : Word

    @Insert
    fun insertWord(word: Word) : Long
}