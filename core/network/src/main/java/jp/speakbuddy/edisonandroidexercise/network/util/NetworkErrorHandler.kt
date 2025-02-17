package jp.speakbuddy.edisonandroidexercise.network.util

import retrofit2.HttpException

object NetworkErrorHandler {
    fun handleNetworkError(error: Throwable): Throwable {
        return when (error) {
            is HttpException -> handleHttpError(error)
            is java.net.UnknownHostException -> Throwable("No internet connection")
            is java.net.SocketTimeoutException -> Throwable("Connection timed out")
            is java.io.IOException -> Throwable("Network error occurred")
            else -> Throwable("Something went wrong")
        }
    }

    private fun handleHttpError(error: HttpException): Throwable {
        return when (error.code()) {
            400 -> Throwable("Bad Request")
            401 -> Throwable("Unauthorized")
            403 -> Throwable("Forbidden")
            404 -> Throwable("Not Found")
            500 -> Throwable("Internal Server Error")
            502 -> Throwable("Bad Gateway")
            503 -> Throwable("Service Unavailable")
            504 -> Throwable("Gateway Timeout")
            else -> Throwable("Server Error: ${error.code()}")
        }
    }
} 