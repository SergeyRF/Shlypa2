package com.example.sergey.shlypa2.utils

import androidx.annotation.MainThread

class TriggerLiveData: SingleLiveEvent<Boolean>() {

    @MainThread
    override fun call() {
        value = true
    }
}