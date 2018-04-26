package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Team

/**
 * Created by sergey on 3/29/18.
 */


class TeamWithScores(val team: Team) {
    var scores = 0
    //keeps scores for players: key - player id, value - scores
    var scoresMap: MutableMap<Long, Int> = mutableMapOf()
}

class RoundDescriptor(var description: Int, var rules: Int, var name:Int, var image:String)

class Settings(var time: Int = 30,
               var word: Int = 5,
               var type: WordType = WordType.EASY,
               var allowRandomWords: Boolean = true,
               var returnSkipedToHat: Boolean = true,
               var minusBal: Boolean = false,
               var numberMinusBal: Int = 1)


enum class WordType(val title : Int){
    EASY(R.string.easy),
    MEDIUM(R.string.medium),
    HARD(R.string.hard),
    VERY_HARD(R.string.very_hard),
    USER(R.string.user)
}

enum class PlayerType {
    STANDARD, USER
}