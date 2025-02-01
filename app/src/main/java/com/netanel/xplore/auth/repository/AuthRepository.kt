package com.netanel.xplore.auth.repository

import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.localDatabase.user.model.UserEntity
import kotlinx.coroutines.flow.Flow


/**
 * Created by netanelamar on 30/11/2024.
 * NetanelCA2@gmail.com
 */
interface AuthRepository {
    suspend fun loginByPhoneNumber(phoneNumber: String): User?
}