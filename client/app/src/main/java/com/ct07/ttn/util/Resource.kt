package com.ct07.ttn.util

sealed class Resource<T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T): Resource<T>(Status.SUCCESS, data)
    class Error<T>(message: String, data: T?=null): Resource<T>(Status.ERROR, data, message)
    class Loading<T>: Resource<T>(Status.LOADING)

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}