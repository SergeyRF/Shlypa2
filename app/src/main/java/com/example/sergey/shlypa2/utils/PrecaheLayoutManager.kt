package com.example.sergey.shlypa2.utils

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by alex on 4/29/18.
 */
class PrecaheLayoutManager(context : Context?) : LinearLayoutManager(context) {

    var extraSpace = 600

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        return extraSpace
    }
}