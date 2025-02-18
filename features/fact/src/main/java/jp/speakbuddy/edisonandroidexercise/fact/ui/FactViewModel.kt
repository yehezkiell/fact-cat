@file:OptIn(ExperimentalCoroutinesApi::class)

package jp.speakbuddy.edisonandroidexercise.fact.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.edisonandroidexercise.domain.usecase.FactCatUseCase
import jp.speakbuddy.edisonandroidexercise.domain.util.DomainResult
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatDetail
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatUiModel
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatUiState
import jp.speakbuddy.edisonandroidexercise.fact.model.ToastState
import jp.speakbuddy.edisonandroidexercise.fact.model.toUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactViewModel @Inject constructor(
    private val usecase: FactCatUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FactCatUiState())
    val uiState = _uiState
        .onStart {
            fetchFact(true)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            FactCatUiState()
        )

    fun updateFact() {
        fetchFact(false)
    }

    private fun fetchFact(isFirstOpen: Boolean) {
        if (!isFirstOpen) {
            showLoadingButton()
        }

        viewModelScope.launch {
            try {
                //Use first for single job, so its canceling the job after retrieval
                //Prevent unnecessary ongoing Flow collection
                val result = usecase
                    .getFact(isFirstOpen)
                    .first()

                when (result) {
                    is DomainResult.Success -> {
                        handleSuccess(result.data.toUiModel())
                    }

                    is DomainResult.Error -> {
                        handleError(result.message)
                    }
                }
            } catch (e: Throwable) {
                showError(e.message)
            }
        }
    }

    private fun handleError(
        errorMessage: String
    ) {
        // Show toast error if:
        // 1. We already have successful data on screen (to preserve existing UI)
        // Otherwise, show full screen error to indicate no data is available
        if (_uiState.value.detail is FactCatDetail.Success) {
            renderToasterError(errorMessage)
            return
        }

        renderFullError(errorMessage)
    }

    private fun handleSuccess(uiModel: FactCatUiModel) {
        _uiState.update {
            FactCatUiState(
                detail = FactCatDetail.Success(uiModel)
            )
        }
    }

    private fun renderFullError(message: String) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                detail = FactCatDetail.Fail(message),
                toasterState = null
            )
        }
    }

    private fun renderToasterError(message: String) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                toasterState = ToastState(
                    toasterMessage = message,
                    //prevent distinct until change
                    toasterTimeMilis = System.currentTimeMillis()
                )
            )
        }
    }

    private fun showLoadingButton() {
        _uiState.update {
            it.copy(
                isLoading = true,
                toasterState = null
            )
        }
    }

    private suspend fun showError(message: String?) {
        _uiState.emit(
            FactCatUiState(
                detail = FactCatDetail.Fail(
                    message ?: "Something went wrong"
                )
            )
        )
    }
}
