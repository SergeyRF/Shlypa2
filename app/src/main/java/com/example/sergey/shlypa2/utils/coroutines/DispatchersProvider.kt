package com.example.sergey.shlypa2.utils.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {
    val uiDispatcher: CoroutineDispatcher
    val ioDispatcher: CoroutineDispatcher
    val computationDispatcher: CoroutineDispatcher
}