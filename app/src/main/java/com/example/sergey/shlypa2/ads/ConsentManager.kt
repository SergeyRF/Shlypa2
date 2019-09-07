package com.example.sergey.shlypa2.ads

import android.content.Context
import android.preference.PreferenceManager
import com.example.sergey.shlypa2.extensions.debug
import com.google.ads.consent.*
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL

class ConsentManager(val context: Context) {

    companion object {
        private const val SAVED_STATUS = "saved_status"
    }

    private var testDeviceId: String? = null
    private var publisherId: String? = null
    private var privacyLink: String? = null

    private var currentConsentStatus = InnerConsentStatus.NOT_SET
    private val consentInfo = ConsentInformation.getInstance(context)
    private var consentForm: ConsentForm? = null


    fun initConsent(publisherId: String, privacyLink: String, testDeviceId: String?) {
        this.testDeviceId = testDeviceId
        this.publisherId = publisherId
        this.privacyLink = privacyLink

        debug {
            with(consentInfo) {
                debugGeography = DebugGeography.DEBUG_GEOGRAPHY_EEA
                addTestDevice(testDeviceId)
            }
        }

        loadStatusFromPrefs()

        if (currentConsentStatus == InnerConsentStatus.NOT_SET || currentConsentStatus == InnerConsentStatus.UNKNOWN) {
            checkConsent()
        }
    }

    fun consentRequired() = consentInfo.isRequestLocationInEeaOrUnknown

    fun showConsentIfNeed(context: Context, buyCallBack: () -> Unit = {}) {
        when (currentConsentStatus) {
            InnerConsentStatus.UNKNOWN -> if (consentForm?.isShowing != true) showConsent(context)
            InnerConsentStatus.NOT_SET -> checkConsent { showConsentIfNeed(context, buyCallBack) }
            else -> return
        }
    }

    fun forceShowConsent(context: Context, buyCallBack: () -> Unit = {}) {
        showConsent(context, buyCallBack)
    }

    fun canShowAds(): Boolean =
            currentConsentStatus == InnerConsentStatus.NO_NEED ||
                    currentConsentStatus == InnerConsentStatus.PERSONALIZED ||
                    currentConsentStatus == InnerConsentStatus.NON_PERSONALIZED

    fun nonPersonalizedOnly(): Boolean = currentConsentStatus == InnerConsentStatus.NON_PERSONALIZED

    /**
     * Check is consent is required
     */
    private fun checkConsent(doOnUnknown: () -> Unit = {}) {
        if (!consentInfo.isRequestLocationInEeaOrUnknown) {
            currentConsentStatus = InnerConsentStatus.NO_NEED
            saveConsentStatus(currentConsentStatus)
            return
        }

        val publisherIds = arrayOf(publisherId)
        consentInfo.requestConsentInfoUpdate(publisherIds, object : ConsentInfoUpdateListener {
            override fun onConsentInfoUpdated(consentStatus: ConsentStatus) {
                currentConsentStatus = when (consentStatus) {
                    ConsentStatus.UNKNOWN -> InnerConsentStatus.UNKNOWN
                    ConsentStatus.PERSONALIZED -> InnerConsentStatus.PERSONALIZED
                    ConsentStatus.NON_PERSONALIZED -> InnerConsentStatus.NON_PERSONALIZED
                }

                saveConsentStatus(currentConsentStatus)
                if (currentConsentStatus == InnerConsentStatus.UNKNOWN) doOnUnknown()
            }

            override fun onFailedToUpdateConsentInfo(errorDescription: String) {}
        })
    }


    private fun saveConsentStatus(status: InnerConsentStatus) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SAVED_STATUS, status.toString())
                .apply()
    }

    private fun loadStatusFromPrefs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val statusString = prefs.getString(SAVED_STATUS, InnerConsentStatus.NOT_SET.toString())
        currentConsentStatus = InnerConsentStatus.valueOf(statusString!!)
    }

    /**
     * Shows consent dialog and saves user choice to shared preferences
     * if user choice ad free version then buyCallBack function will be called
     */
    private fun showConsent(context: Context, buyCallBack: () -> Unit = {}) {
        var privacyUrl: URL? = null
        try {
            privacyUrl = URL(privacyLink)
        } catch (e: MalformedURLException) {
            Timber.e(e)
        }

        val builder = ConsentForm.Builder(context, privacyUrl)
                .withListener(object : ConsentFormListener() {
                    override fun onConsentFormLoaded() {
                        consentForm?.show()
                    }

                    override fun onConsentFormOpened() {}

                    override fun onConsentFormClosed(
                            consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
                        consentForm = null

                        if (userPrefersAdFree == true) {
                            buyCallBack()
                        } else {
                            currentConsentStatus = if (consentStatus == ConsentStatus.PERSONALIZED) {
                                InnerConsentStatus.PERSONALIZED
                            } else {
                                InnerConsentStatus.NON_PERSONALIZED
                            }
                            saveConsentStatus(currentConsentStatus)
                        }
                    }

                    override fun onConsentFormError(errorDescription: String?) {}
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()

        consentForm = builder.build()
        consentForm?.load()
    }

    enum class InnerConsentStatus {
        NO_NEED,
        PERSONALIZED,
        NON_PERSONALIZED,
        UNKNOWN,
        NOT_SET,
        PREMIUM
    }
}