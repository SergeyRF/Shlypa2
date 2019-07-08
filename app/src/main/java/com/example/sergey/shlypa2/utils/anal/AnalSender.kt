package com.example.sergey.shlypa2.utils.anal

import android.content.Context
import android.os.Bundle
import com.flurry.android.FlurryAgent
import com.google.firebase.analytics.FirebaseAnalytics

class AnalSender(val context: Context) {

    companion object {
        private const val TEAMS_CREATED = "teams_created"
        private const val PLAYERS_COUNT = "players_count"
        private const val TEAMS_COUNT = "teams_count"
        private const val RATE_OPENED = "rate_dialog_opened"
        private const val RATE_CANCELED = "rate_dialog_canceled"
        private const val RATE_STARS_SELECTED = "rate_dialog_stars_selected"
        private const val RATE_STARS_COUNT = "rate_dialog_stars_count"
        private const val RATE_NEVER_CLICKED = "rate_never_clicked"

        private const val WORDS_ADDED_AUTOMATTICALLY = "words_added_auto"
        private const val WORD_WRITTEN = "word_written"

        private const val GAME_STARTED = "game_auto_allowed"
        private const val GAME_AUTO_ALLOWED = "game_auto_allowed"
        private const val GAME_WORDS_TYPE = "game_wods_type"

        private const val GAME_LOADED = "game_loaded"
        private const val GAME_FINISHED = "game_finished"

        private const val PLAYER_ADDED = "player_added"
        private const val PLAYER_ADDED_AUTO = "player_added_auto"

        private const val GAME_ALL_WORD_AUTOFILL = "game_all_word_autofill"
    }

    private val firebase = FirebaseAnalytics.getInstance(context)

    fun sendEventTeamsCreated(playersCount: Int, teamsCount: Int) {
        val bundle = Bundle()
        bundle.putInt(PLAYERS_COUNT, playersCount)
        bundle.putInt(TEAMS_COUNT, teamsCount)

        firebase.logEvent(TEAMS_CREATED, bundle)

        FlurryAgent.logEvent(
                TEAMS_CREATED, mapOf(PLAYERS_COUNT to playersCount.toString(),
                TEAMS_COUNT to teamsCount.toString()))
    }

    fun rateDialogOpened() {
        FlurryAgent.logEvent(RATE_OPENED)
        firebase.logEvent(RATE_OPENED, null)
    }

    fun rateDialogNeverClicked() {
        FlurryAgent.logEvent(RATE_NEVER_CLICKED)
        firebase.logEvent(RATE_NEVER_CLICKED, null)
    }

    fun rateDialogCanceled() {
        FlurryAgent.logEvent(RATE_CANCELED)
        firebase.logEvent(RATE_CANCELED, null)
    }

    fun rateStarred(stars: Int) {
        FlurryAgent.logEvent(RATE_STARS_SELECTED, mapOf(RATE_STARS_COUNT to stars.toString()))
        firebase.logEvent(RATE_STARS_SELECTED, Bundle().apply {
            putInt(RATE_STARS_SELECTED, stars)
        })
    }

    fun wordAdded(auto: Boolean) {
        FlurryAgent.logEvent(WORD_WRITTEN, mapOf(WORDS_ADDED_AUTOMATTICALLY to auto.toString()))
    }

    fun playerAdded(auto: Boolean) {
        FlurryAgent.logEvent(PLAYER_ADDED, mapOf(PLAYER_ADDED_AUTO to auto.toString()))
    }

    fun gameStarted(autoAllowed: Boolean, wordType: String, wordAutofill:Boolean = false) {
        FlurryAgent.logEvent(GAME_STARTED, mapOf(
                GAME_AUTO_ALLOWED to autoAllowed.toString(),
                GAME_WORDS_TYPE to wordType,
                GAME_ALL_WORD_AUTOFILL to wordAutofill.toString()
        ))
    }


    fun gameLoaded() {
        firebase.logEvent(GAME_LOADED, null)
        FlurryAgent.logEvent(GAME_LOADED)
    }

    fun gameFinished() {
        firebase.logEvent(GAME_FINISHED, null)
        FlurryAgent.logEvent(GAME_FINISHED)
    }

}