package com.netanel.xplore.localDatabase.user.di

import com.netanel.xplore.localDatabase.user.dao.UserDao
import com.netanel.xplore.localDatabase.user.repository.UserRepository
import com.netanel.xplore.localDatabase.user.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Singleton
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
         return UserRepositoryImpl(userDao)
    }
}
