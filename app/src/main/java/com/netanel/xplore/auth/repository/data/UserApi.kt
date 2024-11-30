package com.netanel.xplore.auth.repository.data

import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.quiz.model.Question
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    @GET("/users")
    suspend fun getUser(@Query(value = "phoneNumber") phoneNumber: String): User
}