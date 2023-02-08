package com.patronusstudio.sisecevirmece2.ui.widgets

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.patronusstudio.sisecevirmece2.BuildConfig
import com.patronusstudio.sisecevirmece2.data.enums.InterstitialAdViewLoadStatusEnum

@Composable
fun BannerAdView(adValue: String) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = adValue
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}


object InterstitialAdView{
    private var mInterstitialAd: InterstitialAd? = null
    fun loadInterstitial(activity:Activity,result:(InterstitialAdViewLoadStatusEnum) -> Unit) {
        InterstitialAd.load(activity.applicationContext,BuildConfig.package_download_interstitial,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    result(InterstitialAdViewLoadStatusEnum.FAILED)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    showInterstitial(activity,result)
                }
            }
        )
    }

    private fun showInterstitial(activity:Activity, result: (InterstitialAdViewLoadStatusEnum) -> Unit) {
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


