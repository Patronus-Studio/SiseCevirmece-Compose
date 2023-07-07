package com.patronusstudio.sisecevirmece2.ui.widgets

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.ads.MaxAdView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.patronusstudio.sisecevirmece2.data.enums.InterstitialAdViewLoadStatusEnum

// TODO: banner da sorun olabilir eski android viewine bir bak
@Composable
fun BannerAdView(adUnitId: String,context:Context) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
        MaxAdView(adUnitId, MaxAdFormat.BANNER, context).also {
            it.loadAd()
        }
    }
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


