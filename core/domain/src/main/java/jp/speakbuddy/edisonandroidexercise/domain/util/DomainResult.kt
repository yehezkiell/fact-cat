package jp.speakbuddy.edisonandroidexercise.domain.util

sealed interface DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>
    data class Error(val message: String) : DomainResult<Nothing>
}