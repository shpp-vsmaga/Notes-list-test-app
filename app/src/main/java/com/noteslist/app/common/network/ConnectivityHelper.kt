package com.noteslist.app.common.network

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class ConnectivityHelper(private val context: Context) {
    var isOnline: Boolean = false
        get() = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) isOnlineOldApi() else field
        private set


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            initNetworkCallback()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initNetworkCallback() {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isOnline = true
                Log.d("svcom", "onAvailable")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isOnline = false
                Log.d("svcom", "onLost")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                isOnline = false
                Log.d("svcom", "onUnavailable")
            }
        }
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    @TargetApi(Build.VERSION_CODES.P)
    private fun isOnlineOldApi(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}