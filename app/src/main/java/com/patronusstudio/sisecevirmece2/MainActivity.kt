package com.patronusstudio.sisecevirmece2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.patronusstudio.sisecevirmece2.nav.ScreenHost
import com.patronusstudio.sisecevirmece2.ui.theme.SiseCevirmeceTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        val configuration = RequestConfiguration.Builder()
            .setTestDeviceIds(Collections.singletonList("B843F895E94E5BEDEAD125878F53D9E6"))
            .build()
        MobileAds.setRequestConfiguration(configuration)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            SiseCevirmeceTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ScreenHost(navController = navController)
                }
            }
        }
    }
}