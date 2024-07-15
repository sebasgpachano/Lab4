package com.example.lab4.data.repository

import com.example.lab4.data.model.ErrorModel

sealed class BaseResponse<T> {
    class Success<T>(val data: T) : BaseResponse<T>()
    class Error<T>(val error: ErrorModel) : BaseResponse<T>()
}