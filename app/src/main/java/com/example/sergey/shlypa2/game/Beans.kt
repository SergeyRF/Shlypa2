package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team

/**
 * Created by sergey on 3/29/18.
 */




class TeamWithScores(val team : Team) {
    var scores = 0
    //keeps scores for players: key - player id, value - scores
    var scoresMap : MutableMap<Long, Int> = mutableMapOf()
}

class RoundDescriptor(var description : String, var rules : String)

class Settings(var time : Int = 30, var word:Int = 5, var dificult:Dificult = Dificult.EASY,
               var autoAddWords:Boolean = false, var minusBal:Boolean = false,
               var numberMinusBal:Int = 1 )

enum class Dificult{ EASY, MEDIUM, HARD, VERY_HARD }

