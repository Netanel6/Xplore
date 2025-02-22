package com.netanel.xplore.auth.ui

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.xplore.auth.repository.AuthRepository
import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.localDatabase.user.converters.toEntity
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.utils.SharedPrefKeys
import com.netanel.xplore.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by netanelamar on 01/11/2024.
 * NetanelCA2@gmail.com
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    var phoneNumber = mutableStateOf("")
    val authState = mutableStateOf<AuthState>(AuthState.Idle)

    fun startUserVerification(phoneNumber: String) {
        authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val user = authRepository.loginByPhoneNumber(phoneNumber)
                if (user!= null) {
                    sharedPreferencesManager.saveString(SharedPrefKeys.TOKEN, user.token)
                    authState.value = AuthState.VerificationCompleted(user)
                }
            } catch (e: Exception) {
                authState.value = AuthState.Error("Phone number incorrect")
            }
        }
    }

    sealed class AuthState {
        data object Idle : AuthState()
        data object Loading : AuthState()
        data class VerificationCompleted(val user: User) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
