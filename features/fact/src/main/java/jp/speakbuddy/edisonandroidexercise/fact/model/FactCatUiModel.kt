package jp.speakbuddy.edisonandroidexercise.fact.model

import jp.speakbuddy.edisonandroidexercise.domain.model.FactCatDomainModel

data class FactCatUiModel(
    val fact: String,
    val length: Int,
    val containsCats: Boolean
)

fun FactCatDomainModel.toUiModel() = FactCatUiModel(fact, length, containsCats)
