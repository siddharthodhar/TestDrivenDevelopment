package com.example.tdd.model

sealed class ApiResponse<out T> {
    data class Success<T>(
        val code: Int,
        val data: T
    ) : ApiResponse<T>()

    data class Error(
        val code: Int,
        val throwable: Throwable
    ) : ApiResponse<Nothing>()
}

