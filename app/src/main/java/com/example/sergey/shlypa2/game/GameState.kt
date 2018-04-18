package com.example.sergey.shlypa2.game

import com.example.sergey.shlypa2.beans.Player
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

    val resultsList: MutableList<RoundResults> = mutableListOf()

    val teams = mutableListOf<Team>()
    var currentTeamPosition = 0


    var currentRoundPosition = -1

    val allWords = mutableListOf<Word>()

    val players = mutableMapOf<String, Player>()

    fun saveRoundResults(results: RoundResults) {
        resultsList.add(results)
    }

    fun createRound() : Round? {
        if(currentRoundPosition >= rounds.size) return null

        val round = Round(allWords)
        round.description = rounds[currentRoundPosition].description
        round.rules = rounds[currentRoundPosition].rules

        return round
    }

}