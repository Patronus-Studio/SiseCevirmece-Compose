package com.patronusstudio.sisecevirmece2

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.patronusstudio.sisecevirmece2.nav.ScreenHost
import com.patronusstudio.sisecevirmece2.ui.theme.SiseCevirmeceTheme
import com.patronusstudio.sisecevirmece2.ui.widgets.SampleErrorNotClosable
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        val configuration = RequestConfiguration.Builder()
            .setTestDeviceIds(Collections.singletonList("B843F895E94E5BEDEAD125878F53D9E6"))
            .build()
        MobileAds.setRequestConfiguration(configuration)
        firebaseAnalytics = Firebase.analytics
        connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            SiseCevirmeceTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ScreenHost(navController = navController)
                    InternetConnectionListener()
                }
            }
        }
    }

    @Composable
    fun InternetConnectionListener() {
        val connectionIsOk = remember { mutableStateOf(false) }
        if (connectionIsOk.value.not()) {
            SampleErrorNotClosable(text = stringResource(R.string.internet_connection_control))
        }
        val networkCallback: NetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                try {
                    if (connectionIsOk.value.not()) {
                        connectionIsOk.value = true
                    }
                } catch (_: java.lang.Exception) {

                }
            }

            override fun onLost(network: Network) {
                try {
                    if (connectionIsOk.value) {
                        connectionIsOk.value = false
                    }
                } catch (_: java.lang.Exception) {
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}
