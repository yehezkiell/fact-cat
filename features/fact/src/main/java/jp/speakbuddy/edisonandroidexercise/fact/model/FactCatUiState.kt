package jp.speakbuddy.edisonandroidexercise.fact.model

data class FactCatUiState(
    val detail: FactCatDetail = FactCatDetail.InitialLoading,
    val isLoading: Boolean = false
)

sealed interface FactCatDetail {
    data class Success(
        val uiModel: FactCatUiModel,
    ) : FactCatDetail

    data class Fail(
        val errorMessage: String
    ) : FactCatDetail

    data object InitialLoading : FactCatDetail
}