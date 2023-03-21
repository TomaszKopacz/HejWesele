package com.hejwesele.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.hejwesele.android.osinfo.OsInfo
import com.hejwesele.internet.InternetConnectionState.AVAILABLE
import com.hejwesele.internet.InternetConnectionState.UNAVAILABLE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class InternetConnectionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val osInfo: OsInfo
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val currentConnectionState = connectivityManager.getCurrentInternetConnectionState()

    fun observeConnectionState() = callbackFlow {
        with(connectivityManager) {
            val callback = networkCallback { connectionState -> trySend(connectionState) }

            if (osInfo.isNOrHigher) {
                registerDefaultNetworkCallback(callback)
            } else {
                val networkRequest = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
                registerNetworkCallback(networkRequest, callback)
            }

            trySend(getCurrentInternetConnectionState())

            awaitClose {
                unregisterNetworkCallback(callback)
            }
        }
    }

    private fun ConnectivityManager.getCurrentInternetConnectionState(): InternetConnectionState {
        var currentState = UNAVAILABLE

        allNetworks.forEach { network ->
            getNetworkCapabilities(network)?.let { capabilities ->
                if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    currentState = AVAILABLE
                    return@forEach
                }
            }
        }

        return currentState
    }

    private fun networkCallback(callback: (InternetConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                callback(AVAILABLE)
            }

            override fun onLost(network: Network) {
                callback(UNAVAILABLE)
            }
        }
    }
}
