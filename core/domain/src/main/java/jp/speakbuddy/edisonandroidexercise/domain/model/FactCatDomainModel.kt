package jp.speakbuddy.edisonandroidexercise.domain.model

import jp.speakbuddy.edisonandroidexercise.data.model.FactCatDataModel

data class FactCatDomainModel(
    val fact: String, val length: Int, val containsCats: Boolean, val showLength: Boolean
)

fun FactCatDataModel.toDomainModel() =
    FactCatDomainModel(fact, length, fact.contains("cats", ignoreCase = true), length > 100)


