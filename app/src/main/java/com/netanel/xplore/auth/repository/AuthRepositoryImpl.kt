package com.netanel.xplore.auth.repository

import com.netanel.xplore.auth.repository.data.UserApi
import com.netanel.xplore.auth.repository.model.User


/**
 * Created by netanelamar on 30/11/2024.
 * NetanelCA2@gmail.com
 */
class AuthRepositoryImpl(private val api: UserApi): AuthRepository {

    override suspend fun getUser(phoneNumber: String): User? {
        val user = api.getUser(phoneNumber)
        return user
    }
}