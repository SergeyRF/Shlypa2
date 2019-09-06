package com.example.sergey.shlypa2.utils.anal

import com.flurry.android.FlurryAgent
import timber.log.Timber

class FlurryFacadeRelease : FlurryFacade {
    override fun logEvent(event: String) {
        FlurryAgent.logEvent(event)
    }

    override fun logEvent(event: String, params: Map<String, String>) {
        FlurryAgent.logEvent(event, params)
    }
}

class FlurryFacadeDebug : FlurryFacade {
    override fun logEvent(event: String) {
        Timber.d("Log flurry event $event")
    }

    override fun logEvent(event: String, params: Map<String, String>) {
        Timber.d("Log flurry event $event")
        Timber.d("With params $params")
    }
}

interface FlurryFacade {

    fun logEvent(event: String)

    fun logEvent(event: String, params: Map<String, String>)
}