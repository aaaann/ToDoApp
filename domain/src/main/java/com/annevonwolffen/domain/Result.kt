package com.annevonwolffen.domain

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Error(val message: String?) : Result<String?>()
}

fun <T> Result<T>.handle(onSuccess: (T) -> Unit = {}, onError: (String?) -> Unit = {}): Unit =
    when (this) {
        is Result.Success -> onSuccess.invoke(this.value)
        is Result.Error -> onError.invoke(this.message)
    }