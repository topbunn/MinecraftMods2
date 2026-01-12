package com.hamit.data

import com.hamit.android.Error
import com.hamit.android.Maintenance
import com.hamit.android.NoInternet
import io.ktor.util.network.UnresolvedAddressException

fun Throwable.exceptionHandle() = when (this) {


    is NoInternet,
    is java.net.UnknownHostException,
    is io.ktor.client.plugins.HttpRequestTimeoutException,
    is io.ktor.client.network.sockets.ConnectTimeoutException,
    is UnresolvedAddressException -> NoInternet

    is Maintenance,
    is javax.net.ssl.SSLHandshakeException,
    is javax.net.ssl.SSLException,
    is java.net.SocketTimeoutException,
    is java.net.ConnectException,
    is io.ktor.client.plugins.ServerResponseException -> Maintenance

    else -> Error
}
