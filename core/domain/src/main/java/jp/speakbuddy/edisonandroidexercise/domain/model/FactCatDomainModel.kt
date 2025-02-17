package jp.speakbuddy.edisonandroidexercise.domain.model

import jp.speakbuddy.edisonandroidexercise.data.model.FactCatDataModel

data class FactCatDomainModel(
    val fact: String,
    val length: Int
)

fun FactCatDataModel.toDomainModel() = FactCatDomainModel(fact, length)


