package jp.speakbuddy.edisonandroidexercise.network

import jp.speakbuddy.edisonandroidexercise.network.service.FactResponse
import jp.speakbuddy.edisonandroidexercise.network.service.FactService
import jp.speakbuddy.edisonandroidexercise.network.util.NetworkErrorHandler
import retrofit2.HttpException
import javax.inject.Inject

interface FactCatNetworkDataSource {
    suspend fun getFact(): Result<FactResponse>
}

class FactCatNetworkDataSourceImpl @Inject constructor(private val service: FactService) :
    FactCatNetworkDataSource {
    override suspend fun getFact(): Result<FactResponse> = try {
        val response = service.getFact()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            Result.success(body)
        } else {
            Result.failure(
                Throwable(
                    NetworkErrorHandler.handleNetworkError(
                        HttpException(response)
                    )
                )
            )
        }
    } catch (e: Exception) {
        Result.failure(NetworkErrorHandler.handleNetworkError(e))
    }
}