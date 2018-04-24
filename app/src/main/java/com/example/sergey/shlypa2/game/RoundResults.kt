package com.example.sergey.shlypa2.game

/**
 * Created by alex on 4/15/18.
 */
class RoundResults(private var results: MutableMap<Long, Int>) {

    fun getScores(): Map<Long, Int> {
        val scoresMap: Map<Long, Int> = results.mapValues { it.value }
        return scoresMap
    }
}