package com.example.tdd.viewmodels.domain

sealed class APIResult<out T>() {
    object Loading: APIResult<Nothing>()
    data class Success<T>(val data: T): APIResult<T>()
    data class Error(val throwable: Throwable): APIResult<Nothing>()

    companion object {
        fun loading(): Loading {
            return Loading
        }

        fun <T> success(data: T): Success<T> {
            return Success(data)
        }

        fun failure(throwable: Throwable): Error {
            return Error(throwable)
        }
    }
}

