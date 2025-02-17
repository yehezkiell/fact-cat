package jp.speakbuddy.edisonandroidexercise.data.model

import jp.speakbuddy.edisonandroidexercise.network.service.FactResponse

data class FactCatDataModel(
    val fact: String,
    val length: Int
)

fun FactResponse.toDataModel() = FactCatDataModel(fact, length)
fun String.toDataModel(): FactCatDataModel {
    return FactCatDataModel(
        this,
        this.length
    )
}
