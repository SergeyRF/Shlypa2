package com.example.sergey.shlypa2

import org.junit.Test
import java.text.DecimalFormat

class FormatTest {

    @Test
    fun testDecimial() {
        val decimalFormat = DecimalFormat("#.##")
        println(decimalFormat.format(30.0f))
        println(decimalFormat.format(30.30f))
        println(decimalFormat.format(30.3f))
    }
}