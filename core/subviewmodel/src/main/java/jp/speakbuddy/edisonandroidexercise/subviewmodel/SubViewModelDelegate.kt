package jp.speakbuddy.edisonandroidexercise.subviewmodel

import kotlinx.coroutines.CoroutineScope

interface SubViewModelDelegate {
    fun registerSubViewModel(viewModelScope: CoroutineScope, mediator: SubViewModelMediator)
}