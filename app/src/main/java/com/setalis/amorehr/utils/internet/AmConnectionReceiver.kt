package com.setalis.amorehr.utils.internet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.setalis.amorehr.R
import com.setalis.amorehr.base.AmActivity
import com.setalis.amorehr.commons.AmLog

class AmConnectionReceiver(val context: AmActivity) : BroadcastReceiver() {

    private var callback: AmConnectionInterface? = null

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        .build()

    override fun onReceive(context: Context, arg1: Intent) {
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        AmLog.e("Internet Check")
    }

    private fun showMessage(message: String) {
        this.callback?.let {
            it.onConnectionChange(message)
        } ?: run {
            AmLog.e("No Callback for Internet State")
        }
    }

    fun registerReceiver(receiver: AmConnectionInterface) {
        this.callback = receiver
    }

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            context.session().isOffline = true
            context.message(context.getString(R.string.system_no_internet_connection))
            AmLog.d("networkcallback called from onLost")
        }
        override fun onUnavailable() {
            AmLog.d("networkcallback called from onUnvailable")
        }
        override fun onLosing(network: Network, maxMsToLive: Int) {
            AmLog.d("networkcallback called from onLosing")
        }
        override fun onAvailable(network: Network) {
            context.session().isOffline = false
            //context.message(Constants.RESPONSE_TYPE.ONLINE)
            AmLog.d("NetworkCallback network called from onAvailable ")
        }
    }

}