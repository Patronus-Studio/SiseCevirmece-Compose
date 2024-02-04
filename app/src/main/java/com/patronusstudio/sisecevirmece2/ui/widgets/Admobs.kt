package com.patronusstudio.sisecevirmece2.ui.widgets

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.patronusstudio.sisecevirmece2.findActivity

@Composable
fun BannerAdView(bannerId: String) {
    AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
        // on below line specifying ad view.
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = bannerId
            loadAd(AdRequest.Builder().build())
        }
    })
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
                try {
                    context.findActivity().let {
                        interstitialAd.show(it)
                    }
                    adClosed()
                } catch (e: Exception) {
                    failedLoad()
                }
            }
        })
}

