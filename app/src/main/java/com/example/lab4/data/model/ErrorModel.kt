package com.example.lab4.data.model


class ErrorModel(
    var error: String = "unknown",
    var errorCode: String = "",
    var message: String = "unknown"
) : BaseModel()