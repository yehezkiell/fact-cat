package jp.speakbuddy.edisonandroidexercise.network.service

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET

interface FactService {
    @GET("/fact")
    suspend fun getFact(): Response<FactResponse>
}

@Serializable
data class FactResponse(
    val fact: String,
    val length: Int
)
