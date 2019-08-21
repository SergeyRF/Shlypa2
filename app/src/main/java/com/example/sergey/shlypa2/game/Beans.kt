package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Team

/**
 * Created by sergey on 3/29/18.
 */


class TeamWithScores(val team: Team) {
    //keeps scores for players: key - player id, value - scores
    var scoresMap: MutableMap<Long, Int> = mutableMapOf()

    fun getScores(): Int = scoresMap.values.sum()
}

class RoundDescriptor(var description: Int, var rules: Int, var name: Int, var image: String)

class Settings(var time: Int = 30,
               var word: Int = 5,
               var typeId: Long = 0,
               var allowRandomWords: Boolean = true,
               var returnSkipedToHat: Boolean = true,
               var penaltyInclude: Boolean = false,
               var penaltyPoint: Int = 1,
               var typeName: String = "",
               var all_word_random: Boolean = false)


enum class WordType(val title: Int) {
    EASY(R.string.easy),
    MEDIUM(R.string.medium),
    HARD(R.string.hard),
    VERY_HARD(R.string.very_hard),
    USER(R.string.user)
}

enum class PlayerType {
    STANDARD, USER
}

enum class AvatarType{
    STANDARD, USER
}