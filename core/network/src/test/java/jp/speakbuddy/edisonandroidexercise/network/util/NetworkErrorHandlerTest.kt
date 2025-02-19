package jp.speakbuddy.edisonandroidexercise.network.util

import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkErrorHandlerTest {

    @Test
    fun handleNetworkError_shouldReturnHttpErrorMessage() {
        // Given
        val httpErrors = mapOf(
            400 to "Bad Request",
            401 to "Unauthorized",
            403 to "Forbidden",
            404 to "Not Found",
            500 to "Internal Server Error",
            502 to "Bad Gateway",
            503 to "Service Unavailable",
            504 to "Gateway Timeout"
        )

        httpErrors.forEach { (code, expectedMessage) ->
            // When
            val response = Response.error<String>(
                code,
                "Error".toResponseBody(null)
            )
            val error = HttpException(response)
            val result = NetworkErrorHandler.handleNetworkError(error)

            // Then
            Assert.assertEquals(expectedMessage, result.message)
        }
    }

    @Test
    fun handleNetworkError_shouldReturnUnknownHttpErrorMessage() {
        // Given
        val response = Response.error<String>(
            418,
            "Error".toResponseBody(null)
        )
        val error = HttpException(response)

        // When
        val result = NetworkErrorHandler.handleNetworkError(error)

        // Then
        Assert.assertEquals("Server Error: 418", result.message)
    }

    @Test
    fun handleNetworkError_shouldReturnNoInternetMessage() {
        // Given
        val error = UnknownHostException()

        // When
        val result = NetworkErrorHandler.handleNetworkError(error)

        // Then
        Assert.assertEquals("No internet connection", result.message)
    }

    @Test
    fun handleNetworkError_shouldReturnTimeoutMessage() {
        // Given
        val error = SocketTimeoutException()

        // When
        val result = NetworkErrorHandler.handleNetworkError(error)

        // Then
        Assert.assertEquals("Connection timed out", result.message)
    }

    @Test
    fun handleNetworkError_shouldReturnNetworkErrorMessage() {
        // Given
        val error = IOException()

        // When
        val result = NetworkErrorHandler.handleNetworkError(error)

        // Then
        Assert.assertEquals("Network error occurred", result.message)
    }

    @Test
    fun handleNetworkError_shouldReturnGenericErrorMessage() {
        // Given
        val error = RuntimeException()

        // When
        val result = NetworkErrorHandler.handleNetworkError(error)

        // Then
        Assert.assertEquals("Something went wrong", result.message)
    }
} 