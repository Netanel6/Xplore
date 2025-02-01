package com.netanel.xplore.localDatabase.user.repository

import com.netanel.xplore.localDatabase.user.dao.UserDao
import com.netanel.xplore.localDatabase.user.model.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val userDao: UserDao): UserRepository {

    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun getUser(userId: String): UserEntity? {
        return userDao.getUserById(userId)
    }

    override suspend fun getUserFlow(userId: String): Flow<UserEntity?> {
        return userDao.getUserFlowById(userId)
    }

    override suspend fun deleteUser(user: UserEntity) {
        userDao.deleteUser(user)
    }
}
