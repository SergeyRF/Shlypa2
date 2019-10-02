package com.example.sergey.shlypa2.beans

import androidx.annotation.StringRes
import com.example.sergey.shlypa2.R

enum class RoundDescriptors(
        @StringRes val description: Int,
        @StringRes val rules: Int,
        @StringRes val nameRes: Int,
        val icon: String
        ) {
    WORD_BY_SENTENCES(
            R.string.round_first_number,
            R.string.round_first,
            R.string.round_first_name,
            "megaphone.png"
    ),
    WORD_BY_GESTURES(
            R.string.round_two_number,
            R.string.round_two,
            R.string.round_two_name,
            "silence.png"
    ),
    WORD_BY_WORD(
            R.string.round_three_number,
            R.string.round_three,
            R.string.round_three_name,
            "one.png"
    )
}