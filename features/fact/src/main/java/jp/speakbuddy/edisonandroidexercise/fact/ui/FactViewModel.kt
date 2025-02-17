@file:OptIn(ExperimentalCoroutinesApi::class)

package jp.speakbuddy.edisonandroidexercise.fact.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.edisonandroidexercise.domain.usecase.FactCatUseCase
import jp.speakbuddy.edisonandroidexercise.domain.util.DomainResult
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatDetail
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatUiState
import jp.speakbuddy.edisonandroidexercise.fact.model.toUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactViewModel @Inject constructor(
    private val usecase: FactCatUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FactCatUiState())
    val uiState: StateFlow<FactCatUiState>
        get() = _uiState

    init {
        fetchFact(true)
    }

    private fun fetchFact(isFirstOpen: Boolean) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }

            //Use first for single job, so its canceling the job after retrieval
            //Prevent unnecessary ongoing Flow collection
            when (val result = usecase.getFact(isFirstOpen).first()) {
                is DomainResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            detail = FactCatDetail.Success(result.data.toUiModel())
                        )
                    }
                }

                is DomainResult.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            detail = FactCatDetail.Fail(result.message)
                        )
                    }
                }
            }
        }
    }

    fun update() {
        fetchFact(false)
    }
}
