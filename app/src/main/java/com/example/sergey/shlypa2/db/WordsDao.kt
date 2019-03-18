package com.example.sergey.shlypa2.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.game.WordType

/**
 * Created by alex on 4/10/18.
 */

@Dao
interface WordsDao {

    @Query("SELECT * FROM ${Contract.WORD_TABLE} " +
            "WHERE ${Contract.WORD_LANG} = :lang")
    fun getAllWords(lang : String = "en") : List<Word>

    @Query("SELECT * FROM ${Contract.WORD_TABLE} " +
            "WHERE ${Contract.WORD_TYPE} = :type " +
            "AND ${Contract.WORD_LANG} = :lang " +
            " ORDER BY RANDOM() LIMIT :wordsLimit")
    fun getRandomWords(wordsLimit : Int, type : WordType = WordType.EASY, lang : String = "en") : List<Word>

    @Query("SELECT * FROM ${Contract.WORD_TABLE} WHERE ${Contract.WORD_ID} = :id")
    fun getWordById(id : Long) : Word

    @Insert
    fun insertWord(word: Word) : Long
}