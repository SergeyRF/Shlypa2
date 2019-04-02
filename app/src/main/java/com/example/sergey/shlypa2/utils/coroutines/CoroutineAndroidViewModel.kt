package com.example.sergey.shlypa2.utils.coroutines

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class CoroutineAndroidViewModel(uiDispatcher: CoroutineDispatcher,
                                app: Application) : AndroidViewModel(app), CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + uiDispatcher

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}