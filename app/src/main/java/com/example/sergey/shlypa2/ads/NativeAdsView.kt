

package com.example.sergey.shlypa2.ads

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
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
            defStyleAttr: Int = 0) : CardView(context, attrs, defStyleAttr) {

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

        nativeAdView.adChoicesView

        nativeAdView.callToActionView = btCta
        nativeAdView.headlineView = tvPrimary
        nativeAdView.mediaView = mediaView
        tvSecondary.show()
        if (adHasOnlyStore(nativeAd)) {
            nativeAdView.storeView = tvSecondary
            secondaryText = store
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView.advertiserView = tvSecondary
            secondaryText = advertiser
        } else {
            secondaryText = ""
        }

        tvPrimary.text = headline
        btCta.text = cta

        //  Set the secondary view to be the star rating if available.
        if (starRating != null && starRating > 0) {
            tvSecondary.hide()
            ratingBar.show()
            ratingBar.max = 5
            nativeAdView.starRatingView = ratingBar
        } else {
            tvSecondary.text = secondaryText
            tvSecondary.show()
            ratingBar.gone()
        }

        if (icon != null) {
            ivIcon.show()
            ivIcon.setImageDrawable(icon.drawable)
        } else {
            ivIcon.gone()
        }

        if (tvBody != null) {
            tvBody.text = body
            nativeAdView.bodyView = tvBody
        }

        nativeAdView.setNativeAd(nativeAd)
    }

    fun destroyNativeAd() {
        nativeAd?.destroy()
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
