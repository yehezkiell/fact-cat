package jp.speakbuddy.edisonandroidexercise.fact.ui.subviewmodel

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatUiState
import jp.speakbuddy.edisonandroidexercise.subviewmodel.SubViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class CatManipulatorSubViewModel @AssistedInject constructor(
    @Assisted viewModelScope: CoroutineScope,
    @Assisted mediator: FactMediator
) : SubViewModel(viewModelScope = viewModelScope, mediator = mediator) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted viewModelScope: CoroutineScope,
            @Assisted mediator: FactMediator
        ): CatManipulatorSubViewModel
    }

    private val _uiState = MutableStateFlow(0)
    val uiState = _uiState.asStateFlow()

    fun randomize() {
        viewModelScope.launch {
            _uiState.emit(Random.nextInt(0, 100))
        }
    }
}