

package com.example.sergey.shlypa2.ads

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.extensions.gone
import com.example.sergey.shlypa2.extensions.hide
import com.example.sergey.shlypa2.extensions.show
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.android.synthetic.main.native_ads_medium.view.*

/** Base class for a template view. *  */
class NativeAdsView @JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var templateType: Int = 0
    private var nativeAd: UnifiedNativeAd? = null

    init {
        initView(context, attrs)
    }

    private fun adHasOnlyStore(nativeAd: UnifiedNativeAd): Boolean {
        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
    }

    fun setNativeAd(nativeAd: UnifiedNativeAd) {
        this.nativeAd = nativeAd

        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        val headline = nativeAd.headline
        val body = nativeAd.body
        val cta = nativeAd.callToAction
        val starRating = nativeAd.starRating
        val icon = nativeAd.icon

        val secondaryText: String

        native_ad_view.callToActionView = btCta
        native_ad_view.headlineView = primary
        native_ad_view.mediaView = media_view
        secondary.show()
        if (adHasOnlyStore(nativeAd)) {
            native_ad_view.storeView = secondary
            secondaryText = store
        } else if (!TextUtils.isEmpty(advertiser)) {
            native_ad_view.advertiserView = secondary
            secondaryText = advertiser
        } else {
            secondaryText = ""
        }

        primary.text = headline
        btCta.text = cta

        //  Set the secondary view to be the star rating if available.
        if (starRating != null && starRating > 0) {
            secondary.hide()
            rating_bar.show()
            rating_bar.max = 5
            native_ad_view.starRatingView = rating_bar
        } else {
            secondary.text = secondaryText
            secondary.show()
            rating_bar.gone()
        }

        if (icon != null) {
            ivIcon.show()
            ivIcon.setImageDrawable(icon.drawable)
        } else {
            ivIcon.gone()
        }

        if (tvBody != null) {
            tvBody.text = body
            native_ad_view.bodyView = tvBody
        }

        native_ad_view.setNativeAd(nativeAd)
    }

    /**
     * To prevent memory leaks, make sure to destroy your ad when you don't need it anymore. This
     * method does not destroy the template view.
     * https://developers.google.com/admob/android/native-unified#destroy_ad
     */
    fun destroyNativeAd() {
        nativeAd!!.destroy()
    }

    private fun initView(context: Context, attributeSet: AttributeSet?) {

        val attributes = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NativeAdsView, 0, 0)

        try {
            templateType = attributes.getResourceId(
                    R.styleable.NativeAdsView_nativeAdsSize, R.layout.native_ads_medium)
        } finally {
            attributes.recycle()
        }

        LayoutInflater.from(context).inflate(templateType, this, true)
    }


}
