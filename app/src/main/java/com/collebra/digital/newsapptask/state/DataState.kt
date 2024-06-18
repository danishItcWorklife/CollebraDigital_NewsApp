package com.collebra.digital.newsapptask.state

sealed class DataState<T>(val data: T? = null, val message: String? = null) {
    class Empty<T> : DataState<T>()
    class Loading<T> : DataState<T>()
    class Success<T>(data: T) : DataState<T>(data, null)
    class Error<T>(message: String) : DataState<T>(null, message)
}