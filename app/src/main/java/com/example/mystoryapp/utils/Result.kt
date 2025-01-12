package com.example.mystoryapp.utils

sealed class Result<R>(
    val data: R? = null,
    val message: String? = null
){
    class Success<R>(data: R): Result<R>(data)
    class Error<R>(error: String): Result<R>(message = error)
    class Loading<R>: Result<R>()
}