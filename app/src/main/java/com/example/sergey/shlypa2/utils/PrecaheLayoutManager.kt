package com.example.sergey.shlypa2.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

/**
 * Created by alex on 4/29/18.
 */
class PrecaheLayoutManager(context : Context?) : androidx.recyclerview.widget.LinearLayoutManager(context) {

    var extraSpace = 600

    override fun getExtraLayoutSpace(state: androidx.recyclerview.widget.RecyclerView.State?): Int {
        return extraSpace
    }

    override fun onLayoutChildren(recycler: androidx.recyclerview.widget.RecyclerView.Recycler?, state: androidx.recyclerview.widget.RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (ex : IndexOutOfBoundsException) {
            Timber.e(ex)
        }
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}