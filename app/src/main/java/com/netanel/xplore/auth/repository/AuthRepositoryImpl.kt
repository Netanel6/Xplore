package com.netanel.xplore.auth.repository

import com.netanel.xplore.auth.repository.data.UserApi
import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.localDatabase.user.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(private val api: UserApi,): AuthRepository {

    override suspend fun loginByPhoneNumber(phoneNumber: String): User? {
        return try {
            val response = api.getUser(phoneNumber)
            if (response.status == "success" && response.data!= null) {
                response.data
            } else {
                throw Exception(response.message?: "Unknown error")
            }
        } catch (e: Exception) {
            null
        }
    }
}