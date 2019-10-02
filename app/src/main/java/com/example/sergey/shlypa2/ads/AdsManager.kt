package com.example.sergey.shlypa2.ads

import android.content.Context
import android.os.Bundle
import com.example.sergey.shlypa2.BuildConfig
import com.example.sergey.shlypa2.data.ConfigsProvider
import com.example.sergey.shlypa2.extensions.debug
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.UnifiedNativeAd
import org.json.JSONObject
import timber.log.Timber


/**
 * Created by alex on 5/8/18.
 */
class AdsManager(
        private val context: Context,
        private val consentManager: ConsentManager,
        private val configsProvider: ConfigsProvider) {

    companion object {
        private const val APP_ID_KEY = "app_id"
        private const val BANNER_ID_KEY = "banner_id"
        private const val TEST_DEVICE_KEY = "test_device_id"
        private const val INTERSTITIAL_ID_KEY = "interstitial_id"
        private const val NATIVE_ID_KEY = "native_ads_id"
        private const val PUBLISHER_ID_KEY = "publisher_id"
        private const val PRIVACY_LINK = "privacy_link"

        private const val BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111"
        private const val INTERSTITIAL_TEST_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val NATIVE_TEST_ID = "ca-app-pub-3940256099942544/2247696110"
    }

    val nativeBeforeTurnEnabled: Boolean
        get() = configsProvider.nativeBeforeTurnEnabled && consentManager.canShowAds()

    val interstitialEnabled: Boolean
        get() = configsProvider.interstitialEnabled && consentManager.canShowAds()

    val interstitialDelayMs: Long
        get() = configsProvider.interstitialDelaySec * 1000

    private var appId: String? = null
    private var bannerId: String? = null
    private var interstitialId: String? = null
    private var nativeAdsId: String? = null
    private var testDeviceId: String? = null
    private var publisherId: String? = null
    private var privacyLink: String? = null

    var initialized = false

    fun initAds() {
        try {
            val jsonObject = loadIds(context) ?: return

            with(jsonObject) {
                appId = optString(APP_ID_KEY)

                testDeviceId = optString(TEST_DEVICE_KEY)
                publisherId = optString(PUBLISHER_ID_KEY)
                privacyLink = optString(PRIVACY_LINK)

                if(BuildConfig.DEBUG) {
                    bannerId = BANNER_TEST_ID
                    interstitialId = INTERSTITIAL_TEST_ID
                    nativeAdsId = NATIVE_TEST_ID
                } else {
                    bannerId = optString(BANNER_ID_KEY)
                    interstitialId = optString(INTERSTITIAL_ID_KEY)
                    nativeAdsId = optString(NATIVE_ID_KEY)
                }
            }

            MobileAds.initialize(context, appId)

            consentManager.initConsent(publisherId ?: "", privacyLink ?: "", testDeviceId)

            initialized = true
        } catch (ex: Exception) {
            //Not initialized, do nothing just log
            Timber.e(ex)
        }
    }

    fun getInterstitial(context: Context): Interstitial? {
        val request = buildRequest() ?: return null

        return interstitialId?.let {
            Interstitial(context, it, request)
        }
    }

    fun getNativeAd(context: Context, onLoaded: (UnifiedNativeAd) -> Unit): AdLoader? {
        val request = buildRequest() ?: return null
        val loader =  AdLoader.Builder(context, nativeAdsId)
                .forUnifiedNativeAd { nativeAd ->
                    onLoaded.invoke(nativeAd)
                }
                .build()

        loader.loadAd(request)
        return loader
    }

    fun getAdRequest() = buildRequest()

    /**
     * Creates ad request if showing ads is allowed by the consentManager
     * otherwise returns null
     */
    private fun buildRequest(): AdRequest? {
        if (consentManager.canShowAds().not()) return null

        with(AdRequest.Builder()) {
            debug {
                addTestDevice(testDeviceId)
            }

            if (consentManager.nonPersonalizedOnly()) {
                val extras = Bundle()
                extras.putString("npa", "1")
                addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            }

            return build()
        }
    }

    private fun loadIds(context: Context): JSONObject? {
        context.assets.open("ads.json").use {
            return JSONObject(String(it.readBytes()))
        }
    }


}