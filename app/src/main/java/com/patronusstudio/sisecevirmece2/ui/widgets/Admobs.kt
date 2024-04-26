package com.patronusstudio.sisecevirmece2.ui.widgets

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.patronusstudio.sisecevirmece2.findActivity

@Composable
fun BannerAdView(bannerId: String,alignment: Alignment) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = alignment){
        AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
            // on below line specifying ad view.
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = bannerId
                loadAd(AdRequest.Builder().build())
            }
        })
    }
}


@Composable
fun AdmobInterstialAd(
    context: Context, addUnitId: String, failedLoad: () -> Unit, adClosed: () -> Unit
) {
    InterstitialAd.load(context,
        addUnitId,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                failedLoad()
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                interstitialAd.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        adClosed()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        super.onAdFailedToShowFullScreenContent(p0)
                        failedLoad()
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                    }
                }
                context.findActivity().let {
                    interstitialAd.show(it)
                }
            }
        })
}

