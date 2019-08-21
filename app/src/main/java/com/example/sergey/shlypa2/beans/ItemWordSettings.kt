package com.example.sergey.shlypa2.beans

data class ItemWordSettings(
        val autoFill: Boolean,
        val allowRandom: Boolean,
        val typesList: List<Type>,
        val selectedType: Type
)