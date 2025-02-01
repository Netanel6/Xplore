package com.netanel.xplore.auth.repository

import com.netanel.xplore.auth.repository.data.UserApi
import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.localDatabase.user.converters.toDomain
import com.netanel.xplore.localDatabase.user.converters.toEntity
import com.netanel.xplore.localDatabase.user.dao.UserDao
import com.netanel.xplore.localDatabase.user.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext


/**
 * Created by netanelamar on 30/11/2024.
 * NetanelCA2@gmail.com
 */
class AuthRepositoryImpl(private val api: UserApi, private val userDao: UserDao): AuthRepository {

    override suspend fun loginByPhoneNumber(phoneNumber: String): User? {
        val response = api.getUser(phoneNumber)
        if (response.status == "success" && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Unknown error")
        }
    }
}