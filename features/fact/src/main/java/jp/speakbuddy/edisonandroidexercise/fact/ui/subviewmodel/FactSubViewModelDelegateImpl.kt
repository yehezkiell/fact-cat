package jp.speakbuddy.edisonandroidexercise.fact.ui.subviewmodel

import jp.speakbuddy.edisonandroidexercise.subviewmodel.SubViewModelMediator
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class FactSubViewModelDelegateImpl @Inject constructor(private val catManipulator: CatManipulatorSubViewModel.Factory) :
    FactSubViewModelDelegate {

    private lateinit var mMediator: FactMediator

    private lateinit var mViewModelScope: CoroutineScope

    override val catManipulatorSubViewModel: CatManipulatorSubViewModel by lazy {
        catManipulator.create(viewModelScope = mViewModelScope, mediator = mMediator)
    }

    override fun registerSubViewModel(
        viewModelScope: CoroutineScope,
        mediator: SubViewModelMediator
    ) {
        this.mViewModelScope = viewModelScope
        this.mMediator = mediator as FactMediator
    }

}