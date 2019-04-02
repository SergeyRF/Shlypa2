package com.example.sergey.shlypa2.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeSafe(owner: LifecycleOwner, block: (T) -> Unit) {
    this.observe(owner, Observer {
        it?.let { block(it) }
    })
}