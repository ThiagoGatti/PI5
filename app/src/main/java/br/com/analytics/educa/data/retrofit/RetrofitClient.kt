package br.com.analytics.educa.data.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Inet4Address
import java.net.NetworkInterface

val BASE_URL = if (getLocalIpAddress().startsWith("10.0.2")) {
    "http://10.0.2.2/"
} else {
    "http://educaserver.ddns.net/"
}

class RetrofitClient {
    companion object {
        private lateinit var INSTANCE: Retrofit
        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build()

            if (!::INSTANCE.isInitialized) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                    .build()
            }
            return INSTANCE
        }

        fun <S> createService(serviceClass: Class<S>): S {
            return getRetrofitInstance().create(serviceClass)
        }
    }
}

fun getLocalIpAddress(): String {
    try {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val inetAddresses = interfaces.nextElement().inetAddresses
            while (inetAddresses.hasMoreElements()) {
                val inetAddress = inetAddresses.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    println("teste ${inetAddress.hostAddress}")
                    return inetAddress.hostAddress ?: ""
                }
            }
        }
    } catch (e: Exception) {
        println("teste")
        e.printStackTrace()
    }
    return ""
}