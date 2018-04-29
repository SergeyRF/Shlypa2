package com.example.sergey.shlypa2.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.WordType

/**
 * Created by alex on 4/10/18.
 */

@Dao
interface WordsDao {

    @Query("SELECT * FROM ${Contract.WORD_TABLE}")
    fun getAllWords() : List<Word>

    @Query("SELECT * FROM ${Contract.WORD_TABLE} WHERE ${Contract.WORD_TYPE} = :type" +
            " ORDER BY RANDOM() LIMIT :wordsLimit")
    fun getRandomWords(wordsLimit : Int, type : WordType = WordType.EASY) : List<Word>

    @Query("SELECT * FROM ${Contract.WORD_TABLE} WHERE ${Contract.WORD_ID} = :id")
    fun getWordById(id : Long) : Word

    @Insert
    fun insertWord(word: Word) : Long
}