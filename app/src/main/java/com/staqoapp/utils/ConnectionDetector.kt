package com.staqoapp.utils

import android.content.Context
import android.net.ConnectivityManager

class ConnectionDetector(val context: Context) {

    fun isInternetOn(): Boolean {
        val cm = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val activeNetwork = cm.activeNetworkInfo
        return if (activeNetwork != null) {
            when (activeNetwork.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    true
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    true
                }
                else -> activeNetwork.type == ConnectivityManager.TYPE_MOBILE_DUN
            }
        } else {
            false
        }
    }
}