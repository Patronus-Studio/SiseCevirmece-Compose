package com.patronusstudio.sisecevirmece2.ui.widgets

import android.app.Activity
import android.view.Gravity
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.patronusstudio.sisecevirmece2.data.enums.InterstitialAdViewLoadStatusEnum
import com.patronusstudio.sisecevirmece2.data.utils.getActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

// TODO: banner da sorun olabilir eski android viewine bir bak
@Composable
fun BannerAdView(adUnitId: String) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            context.getActivity()
            val bannerView = MaxAdView(adUnitId, MaxAdFormat.BANNER, context).apply {
                this.loadAd()
                this.gravity = Gravity.BOTTOM
            }
            FrameLayout(context).apply {
                this.addView(bannerView)
            }
        }
    )
}


object InterstitialAdView {
    private var mInterstitialAd: InterstitialAd? = null
    fun loadInterstitial(
        activity: Activity,
        adsId: String,
        result: (InterstitialAdViewLoadStatusEnum) -> Unit
    ) {
        InterstitialAd.load(activity.applicationContext, adsId, AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    result(InterstitialAdViewLoadStatusEnum.FAILED)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    showInterstitial(activity, result)
                }
            }
        )
    }

    private fun showInterstitial(
        activity: Activity,
        result: (InterstitialAdViewLoadStatusEnum) -> Unit
    ) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(e: AdError) {
                    mInterstitialAd = null
                    result(InterstitialAdViewLoadStatusEnum.FAILED)
                }

                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                    result(InterstitialAdViewLoadStatusEnum.DISSMISSED)
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    result(InterstitialAdViewLoadStatusEnum.SHOWED)
                }
            }
            mInterstitialAd?.show(activity)
        }
    }

    fun removeInterstitial() {
        mInterstitialAd?.fullScreenContentCallback = null
        mInterstitialAd = null
    }
}

object ApplovinUtils {

    private lateinit var interstitialAd: MaxInterstitialAd
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    @Composable
    fun CreateInterstitialAd(
        adUnitId: String,
        onAdShowed: (() -> Unit)? = null,
        onAdClosed: (() -> Unit)? = null,
        onAdLoadFailed: (() -> Unit)? = null,
    ) {
        var retryAttempt = 0.0
        AndroidView(factory = { context ->
            if (this::interstitialAd.isInitialized.not()) {
                interstitialAd = MaxInterstitialAd(adUnitId, context.getActivity())
            } else {
                if (interstitialAd.adUnitId != adUnitId) {
                    interstitialAd = MaxInterstitialAd(adUnitId, context.getActivity())
                }
            }
            FrameLayout(context).also {
                interstitialAd.setListener(object : MaxAdListener {
                    override fun onAdLoaded(p0: MaxAd?) {
                        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'
                        // Reset retry attempt
                        retryAttempt = 0.0
                        if (interstitialAd.isReady) {
                            interstitialAd.showAd()
                        } else {
                            interstitialAd.loadAd()
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
                        onAdLoadFailed?.let { it1 -> it1() }
                    }

                    override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
                        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
                        interstitialAd.loadAd()
                    }

                })
                // Load the first ad
                interstitialAd.loadAd()
            }
        })
    }
}


