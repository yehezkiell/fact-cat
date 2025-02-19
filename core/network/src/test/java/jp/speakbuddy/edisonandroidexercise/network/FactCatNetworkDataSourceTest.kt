package jp.speakbuddy.edisonandroidexercise.network

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import jp.speakbuddy.edisonandroidexercise.network.service.FactResponse
import jp.speakbuddy.edisonandroidexercise.network.service.FactService
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class FactCatNetworkDataSourceTest {

    private lateinit var networkDataSource: FactCatNetworkDataSource

    @RelaxedMockK
    private lateinit var service: FactService

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)
        networkDataSource = FactCatNetworkDataSourceImpl(service)
    }

    private val mockFactResponse
        get() = FactResponse(
            fact = "fact 1",
            length = 10
        )

    @Test
    fun getFactNetworkDataSource_shouldReturnSuccess() = runTest {
        // Given
        coEvery { 
            service.getFact() 
        } returns Response.success(mockFactResponse)

        // When
        val result = networkDataSource.getFact()

        // Then
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(mockFactResponse, result.getOrNull())
    }

    @Test
    fun getFactNetworkDataSource_shouldReturnErrorWhenHttpError() = runTest {
        // Given
        coEvery { 
            service.getFact() 
        } returns Response.error(404, "Not found".toResponseBody(null))

        // When
        val result = networkDataSource.getFact()

        // Then
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull()?.message?.contains("Not Found") == true)
    }

    @Test
    fun getFactNetworkDataSource_shouldReturnErrorWhenNetworkError() = runTest {
        // Given
        coEvery { 
            service.getFact() 
        } throws IOException("Network error")

        // When
        val result = networkDataSource.getFact()

        // Then
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull()?.message?.contains("Network error") == true)
    }

    @Test
    fun getFactNetworkDataSource_shouldReturnErrorWhenNullBody() = runTest {
        // Given
        coEvery { 
            service.getFact() 
        } returns Response.success(null)

        // When
        val result = networkDataSource.getFact()

        // Then
        Assert.assertTrue(result.isFailure)
    }
}