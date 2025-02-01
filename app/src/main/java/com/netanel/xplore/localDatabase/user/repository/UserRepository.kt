package com.netanel.xplore.localDatabase.user.repository

import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.localDatabase.user.model.UserEntity
import kotlinx.coroutines.flow.Flow


/**
 * Created by netanelamar on 01/02/2025.
 * NetanelCA2@gmail.com
 */
interface UserRepository {
    suspend fun insertUser(user: UserEntity)
    suspend fun getUserFlow(userId: String): Flow<UserEntity?>
    suspend fun getUser(userId: String): UserEntity?
    suspend fun deleteUser(user: UserEntity)
}