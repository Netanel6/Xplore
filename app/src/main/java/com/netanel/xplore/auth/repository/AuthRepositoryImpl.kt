package com.netanel.xplore.auth.repository

import com.netanel.xplore.auth.repository.data.UserApi
import com.netanel.xplore.auth.repository.model.User


/**
 * Created by netanelamar on 30/11/2024.
 * NetanelCA2@gmail.com
 */
class AuthRepositoryImpl(private val api: UserApi): AuthRepository {

    override suspend fun getUser(phoneNumber: String): User {
        val response = api.getUser(phoneNumber)
        if (response.status == "success" && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Unknown error")
        }
    }
}