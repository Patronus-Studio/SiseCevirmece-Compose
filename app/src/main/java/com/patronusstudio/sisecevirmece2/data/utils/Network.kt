package com.patronusstudio.sisecevirmece2.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


fun hasInternet(context: Context): Boolean? {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        if (capabilities != null) {
            return if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_SUPL)) true else if (capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                )
            ) true
            else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) true
            else capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }
    } else {
        return try {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        } catch (e: NullPointerException) {
            false
        }
    }
    return false
}