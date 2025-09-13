package jp.speakbuddy.edisonandroidexercise.subviewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


abstract class SubViewModel(
    protected val viewModelScope: CoroutineScope,
    protected val mediator: SubViewModelMediator
) {

    companion object {

        /***
         * helper function to process something in a coroutine
         * @param context by default [viewModelScope.coroutineContext]
         * @param block
         */
        @JvmStatic
        protected fun SubViewModel.launch(
            context: CoroutineContext? = viewModelScope.coroutineContext,
            block: suspend CoroutineScope.() -> Unit
        ) {
            viewModelScope.launch(context = context ?: EmptyCoroutineContext) {
                block()
            }
        }

        /***
         * helper function to get mediator base on type
         * @return inheritance subview model mediator
         */
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        protected fun <T : SubViewModelMediator> SubViewModel.getMediator(): Lazy<T> =
            lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                mediator as T
            }
    }
}