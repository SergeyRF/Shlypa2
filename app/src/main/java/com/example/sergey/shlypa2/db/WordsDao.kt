package com.example.sergey.shlypa2.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sergey.shlypa2.beans.Word

/**
 * Created by alex on 4/10/18.
 */

@Dao
interface WordsDao {

    @Query("SELECT * FROM ${Contract.WORD_TABLE} ") //fixme
    fun getAllWords(): List<Word>

    @Query("SELECT * FROM ${Contract.WORD_TABLE} " +
            "WHERE ${Contract.WORD_TYPE} = :typeId " +
            "ORDER BY RANDOM() LIMIT :wordsLimit") //fixme
    fun getRandomWords(wordsLimit: Int, typeId: Long): List<Word>

    @Query("SELECT * FROM ${Contract.WORD_TABLE} " +
            "WHERE ${Contract.WORD_TYPE} = :typeId ")
    fun getWordsOfType(typeId: Long): List<Word>

    @Query("SELECT * FROM ${Contract.WORD_TABLE} WHERE ${Contract.WORD_ID} = :id")
    fun getWordById(id: Long): Word

    @Insert
    fun insertWord(word: Word): Long
}