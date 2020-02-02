package com.githubrepo.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.githubrepo.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException

class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {
    private val applicationContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable())
            throw NoInternetException("Make Sure You have Active Data Connection")

        try {
            return chain.proceed(chain.request())
        } catch (e: SocketTimeoutException) {
            throw NoInternetException("Failed To Connection... Make your Server is running")
        }

    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }
}