package com.patronusstudio.sisecevirmece2.data.utils

import android.app.Activity
import android.os.Handler
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import java.util.concurrent.TimeUnit
import kotlin.math.pow

object ApplovinUtils {

    private lateinit var interstitialAd:MaxInterstitialAd

    fun createInterstitialAd(
        activity: Activity,
        adUnitId: String,
        onAdLoaded: ((MaxInterstitialAd) -> Unit)? = null,
        onAdShowed: (() -> Unit)? = null,
        onAdClosed: (() -> Unit)? = null
    ) {
        var retryAttempt = 0.0
        if(this::interstitialAd.isInitialized.not()){
            interstitialAd = MaxInterstitialAd(adUnitId, activity)
        }else{
            if(interstitialAd.adUnitId != adUnitId || interstitialAd.activity != activity){
                interstitialAd = MaxInterstitialAd(adUnitId, activity)
            }
        }
        interstitialAd.setListener(object : MaxAdListener {
            override fun onAdLoaded(p0: MaxAd?) {
                // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'
                // Reset retry attempt
                retryAttempt = 0.0
                onAdLoaded?.let {
                    onAdLoaded(interstitialAd)
                }
            }

            override fun onAdDisplayed(p0: MaxAd?) {
                onAdShowed?.let {
                    onAdShowed()
                }
            }

            override fun onAdHidden(p0: MaxAd?) {
                // Interstitial ad is hidden. Pre-load the next ad
                onAdClosed?.let {
                    onAdClosed()
                }
            }

            override fun onAdClicked(p0: MaxAd?) {
            }

            override fun onAdLoadFailed(p0: String?, p1: MaxError?) {
                // Interstitial ad failed to load
                // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)
                retryAttempt++
                val delayMillis =
                    TimeUnit.SECONDS.toMillis(2.0.pow(6.0.coerceAtMost(retryAttempt)).toLong())
                Handler().postDelayed({ interstitialAd.loadAd() }, delayMillis)
            }

            override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
                // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
                interstitialAd.loadAd()
            }

        })
        // Load the first ad
        interstitialAd.loadAd()
    }
}