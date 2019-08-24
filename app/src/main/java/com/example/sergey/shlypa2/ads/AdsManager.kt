package com.example.sergey.shlypa2.ads

import android.content.Context
import android.os.Bundle
import com.example.sergey.shlypa2.BuildConfig
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import org.json.JSONObject
import timber.log.Timber


/**
 * Created by alex on 5/8/18.
 */
class AdsManager(
        private val context: Context,
        private val consentManager: ConsentManager) {

    companion object {
        private const val APP_ID_KEY = "app_id"
        private const val BANNER_ID_KEY = "banner_id"
        private const val TEST_DEVICE_KEY = "test_device_id"
        private const val INTERSTITIAL_ID_KEY = "interstitial_id"
        private const val PUBLISHER_ID_KEY = "publisher_id"
        private const val PRIVACY_LINK = "privacy_link"

        private const val BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111"
        private const val INTERSTITIAL_TEST_ID = "ca-app-pub-3940256099942544/1033173712"
    }

    private var appId: String? = null
    private var bannerId: String? = null
    private var interstitialId: String? = null
    private var testDeviceId: String? = null
    private var publisherId: String? = null
    private var privacyLink: String? = null

    var initialized = false

    fun initAds() {
        try {
            val jsonObject = loadIds(context) ?: return
            Timber.d("TESTING json object $jsonObject")
            appId = jsonObject.optString(APP_ID_KEY)
            bannerId = jsonObject.optString(BANNER_ID_KEY)
            interstitialId = jsonObject.optString(INTERSTITIAL_ID_KEY)
            testDeviceId = jsonObject.optString(TEST_DEVICE_KEY)
            publisherId = jsonObject.optString(PUBLISHER_ID_KEY)
            privacyLink = jsonObject.optString(PRIVACY_LINK)

            MobileAds.initialize(context, appId)

            consentManager.publisherId = publisherId
            consentManager.testDeviceId = testDeviceId
            consentManager.privacyLink = privacyLink
            consentManager.initConsent(context)

            initialized = true
        } catch (ex: Exception) {
            //Not initialized, do nothing just log
            Timber.e(ex)
        }
    }

    fun checkConsent(context: Context) {
        consentManager.showConsentIfNeed(context)
    }

    fun createBannerRequest(): AdRequest? {
        if(consentManager.canShowAds().not()) {
            return null
        }
        val adRequestBuilder = AdRequest.Builder()
        if (BuildConfig.DEBUG) {
            adRequestBuilder.addTestDevice(testDeviceId)
        }

        if(consentManager.nonPersonalizedOnly()) {
            val extras = Bundle()
            extras.putString("npa", "1")
            adRequestBuilder.addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
        }

        return adRequestBuilder.build()
    }

    fun getBannerId(): String? {
        return bannerId
    }

    fun getInterstitial(context: Context): Interstitial? {
        if(consentManager.canShowAds().not()){
            return null
        }

        val builder = AdRequest.Builder()
        if(BuildConfig.DEBUG) {
            builder.addTestDevice(testDeviceId)
        }

        if(consentManager.nonPersonalizedOnly()) {
            val extras = Bundle()
            extras.putString("npa", "1")
            builder.addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
        }

        return interstitialId?.let {
            Interstitial(context, it, builder.build())
        }
    }

    private fun loadIds(context: Context): JSONObject? {
        context.assets.open("ads.json").use {
            return JSONObject(String(it.readBytes()))
        }
    }


}