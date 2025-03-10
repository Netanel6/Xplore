package com.netanel.xplore.utils

data class ServerResponse<T>(
    val status: String,
    val code: Int,
    val message: String? = null,
    val data: T? = null
)