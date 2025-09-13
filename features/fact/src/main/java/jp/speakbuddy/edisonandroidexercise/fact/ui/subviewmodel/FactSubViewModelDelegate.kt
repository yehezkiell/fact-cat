package jp.speakbuddy.edisonandroidexercise.fact.ui.subviewmodel

import jp.speakbuddy.edisonandroidexercise.subviewmodel.SubViewModelDelegate
import kotlinx.coroutines.CoroutineScope

interface FactSubViewModelDelegate : SubViewModelDelegate {
    val catManipulatorSubViewModel: CatManipulatorSubViewModel
}