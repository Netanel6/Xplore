package com.netanel.xplore.auth.repository

import com.netanel.xplore.auth.repository.model.User


/**
 * Created by netanelamar on 30/11/2024.
 * NetanelCA2@gmail.com
 */
interface AuthRepository {
    suspend fun getUser(phoneNumber: String): User?
}