package com.example.sergey.shlypa2.utils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class SerialJob(val scope: CoroutineScope) {
    private var job: Job? = null

    infix fun set(job: Job) {
        this.job?.cancel()
        this.job = job
    }

    fun offer(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend () -> Unit) {
        if(this.job == null || this.job?.isActive == false) {
            this.job = scope.launch(context) { block() }
        }
    }
}