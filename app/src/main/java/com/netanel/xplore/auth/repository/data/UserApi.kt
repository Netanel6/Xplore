package com.netanel.xplore.auth.repository.data

import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.utils.ServerResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("/users/{phoneNumber}")
    suspend fun getUser(@Path("phoneNumber") phoneNumber: String): ServerResponse<User>
}