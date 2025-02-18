package jp.speakbuddy.edisonandroidexercise.fact.model

data class ToastState(
    val toasterMessage: String = "",
    val toasterTimeMilis: Long = 0L
)

data class FactCatUiState(
    val detail: FactCatDetail = FactCatDetail.InitialLoading,
    val isLoading: Boolean = false,
    val toasterState: ToastState? = null
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