package jp.speakbuddy.edisonandroidexercise.network.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
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
