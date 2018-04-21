package com.example.sergey.shlypa2.beans

/**
 * Created by alex on 4/10/18.
 */
object Contract {
    const val DB_VERSION = 13
    const val DB_NAME = "shlyapa_db"

    //Players table columns
    const val PLAYER_TABLE = "players"
    const val PLAYER_ID = "player_id"
    const val PLAYER_AVATAR = "player_avatar"
    const val PLAYER_NAME = "player_name"
    const val PLAYER_TYPE = "player_type"

    //Word table columns
    const val WORD_TABLE = "words"
    const val WORD_ID = "word_id"
    const val WORD_COLUMN = "word_itself"
    const val WORD_LANG = "word_lang"
    const val WORD_TYPE = "word_type"

    //State table columns
    const val STATE_TABLE = "states"
    const val STATE_ID = "state_id"
    const val STATE_GAME_ID = "state_game_id"
    const val STATE_STRING = "state_string"
    const val STATE_TIME = "state_time"

    enum class WordType{
        EASY, MEDIUM, HARD, VERY_HARD, USER
    }

    enum class PlayerType {
        STANDARD, USER
    }
}