package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.beans.Word

/**
 * Created by alex on 4/10/18.
 */
class GameState {
    var rounds = listOf(RoundDescriptor("First round", "Слово - Словами\n" +
            "Необходимо объяснить" +
            " своей команде как можно больше слов за отведенное время. Нельзя иcпользовать " +
            "однокоренные, похожие по звучанию слова, использовать иностранные слова, перевод которых " +
            "означает загаданное слово. Игра продолжается пока в шляпе не останется слов."),
            RoundDescriptor("Second round", "Слово - Жестами\n" +
                    "Слова объясняются жестами как в игре Крокодил. Нельзя ничего произносить вслух," +
                    " использовать предметы, использовать других игроков, показывать на что-либо."),
            RoundDescriptor("Third round", "Слово - Словом\n" +
                    "Необходимо придумать к слову ассоциацию и произнести одно слово обозначающее" +
                    " её. Запрещаются любые жесты и звуки."))

    var gameId = 0

    var settings : Settings = Settings()

    val resultsList: MutableList<RoundResults> = mutableListOf()

    val teams = mutableListOf<Team>()
    var currentTeamPosition = 0


    var currentRoundPosition = -1
    var currentRound : Round? = null

    val allWords = mutableListOf<Word>()

    val savedTime = System.currentTimeMillis()

    //todo keep players by id instead of name
    val players = mutableMapOf<String, Player>()

    fun saveRoundResults(results: RoundResults) {
        resultsList.add(results)
    }

    fun createRound() : Boolean {
        if(currentRoundPosition >= rounds.size) {
            currentRound = null
            return false
        }

        currentRound = Round(allWords)
        currentRound!!.description = rounds[currentRoundPosition].description
        currentRound!!.rules = rounds[currentRoundPosition].rules

        return true
    }

}