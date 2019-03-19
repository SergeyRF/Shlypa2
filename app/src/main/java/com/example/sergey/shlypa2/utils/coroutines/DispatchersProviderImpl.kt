package com.example.sergey.shlypa2.utils.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DispatchersProviderImpl: DispatchersProvider {
    override val uiDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
    override val computationDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default
}