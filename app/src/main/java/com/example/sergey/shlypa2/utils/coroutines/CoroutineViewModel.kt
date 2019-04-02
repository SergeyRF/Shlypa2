package com.example.sergey.shlypa2.utils.coroutines

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class CoroutineViewModel(uiDispatcher: CoroutineDispatcher) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + uiDispatcher

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}