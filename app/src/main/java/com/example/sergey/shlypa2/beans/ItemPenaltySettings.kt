package com.example.sergey.shlypa2.beans

data class ItemPenaltySettings(
        val penaltyCheck: Boolean,
        val min: Int,
        val max: Int,
        val progress: Int
)