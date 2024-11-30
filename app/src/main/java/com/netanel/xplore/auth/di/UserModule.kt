package com.netanel.xplore.auth.di

import com.netanel.xplore.auth.repository.AuthRepository
import com.netanel.xplore.auth.repository.AuthRepositoryImpl
import com.netanel.xplore.auth.repository.data.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


/**
 * Created by netanelamar on 30/11/2024.
 * NetanelCA2@gmail.com
 */
@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(userApi: UserApi): AuthRepository {
        return AuthRepositoryImpl(userApi)
    }
}