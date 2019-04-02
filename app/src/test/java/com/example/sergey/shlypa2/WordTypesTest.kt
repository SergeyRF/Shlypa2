package com.example.sergey.shlypa2

import androidx.room.Room
import com.example.sergey.shlypa2.beans.Lang
import com.example.sergey.shlypa2.beans.Type
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.db.Contract
import com.example.sergey.shlypa2.db.DataBase
import com.example.sergey.shlypa2.db.TypesDao
import com.example.sergey.shlypa2.db.WordsDao
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class WordTypesTest {

    private lateinit var db: DataBase
    private lateinit var typesDao: TypesDao
    private lateinit var wordsDao: WordsDao

    @Before
    fun setup() {
        db = Room.databaseBuilder(RuntimeEnvironment.application, DataBase::class.java, Contract.DB_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        typesDao = db.typesDap()
        wordsDao = db.wordDao()
    }

    @Test
    fun insertTypeTest() {
        val type = Type(0, "Easy", Lang.EN)
        val id = typesDao.insertType(type)
        print("id is $id")
    }

    @Test
    fun getAllTypesTest() {
        val type = Type(0, "Easy", Lang.EN)
        typesDao.insertType(type)
        val types = typesDao.getAllTypes()
        assertEquals(1, types.size)
    }

    @Test
    fun getWordsOfTypeTest() {
        val type1 = Type(0, "Easy", Lang.EN)
        val type2 = Type(0, "Hard", Lang.EN)
        val type1Id = typesDao.insertType(type1)
        val type2Id = typesDao.insertType(type2)

        val wordOne = Word("first", 0, type1Id)
        wordsDao.insertWord(wordOne)

        val wordTwo = Word("second", 0, type2Id)
        val wordThree = Word("third", 0, type2Id)

        wordsDao.insertWord(wordTwo)
        wordsDao.insertWord(wordThree)

        val wordsOfFirstType = wordsDao.getWordsOfType(type1Id)
        assertEquals(1, wordsOfFirstType.size)
        assertEquals(wordOne.word, wordsOfFirstType[0].word)

        val wordsOfSecondType = wordsDao.getWordsOfType(type2Id)
        assertEquals(2, wordsOfSecondType.size)
    }
}