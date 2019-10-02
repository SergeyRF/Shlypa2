package com.example.sergey.shlypa2.beans

import androidx.room.*
import com.example.sergey.shlypa2.db.Contract

/**
 * Created by alex on 4/10/18.
 */

@Entity(tableName = Contract.WORD_TABLE,
        foreignKeys = [ForeignKey(entity = Type::class,
                parentColumns = [Contract.TYPE_ID], childColumns = [Contract.WORD_TYPE])])
class Word(@ColumnInfo(name = Contract.WORD_COLUMN)
           var word: String = "",
           @PrimaryKey(autoGenerate = true)
           @ColumnInfo(name = Contract.WORD_ID)
           var id: Long = 0,
           @ColumnInfo(name = Contract.WORD_TYPE)
           var type: Long = 0,
           @Ignore
           var addedBy: Long = 0) {

    @Ignore
    var play: Boolean = false
    @Ignore
    var right: Boolean = false

    @Ignore
    var answeredBy = 0L

    override fun equals(other: Any?): Boolean {
        return if(other is Word) {
            id == other.id || word == other.word
        } else false
    }

    override fun toString(): String {
        return "words $word id $id"
    }
}