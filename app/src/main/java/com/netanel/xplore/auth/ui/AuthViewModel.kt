package com.netanel.xplore.auth.ui

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.xplore.R
import com.netanel.xplore.auth.repository.AuthRepository
import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.localDatabase.user.converters.toEntity
import com.netanel.xplore.utils.SharedPrefKeys
import com.netanel.xplore.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by netanelamar on 01/11/2024.
 * NetanelCA2@gmail.com
 */@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    var phoneNumber = mutableStateOf("")
    var name = mutableStateOf("")
    val snackbarHostState = SnackbarHostState()
    var authState = mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun startUserVerification(context: Context) {
        authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val user = authRepository.loginByPhoneNumber(phoneNumber.value)

                if (user != null) {
                    authRepository.insertUser(user.toEntity())
                    sharedPreferencesManager.saveString(SharedPrefKeys.TOKEN, user.token)
                    authState.value = AuthState.VerificationCompleted(user)
                }
            } catch (e: Exception) {
                val errorMessage = context.getString(R.string.phone_num_incorrect)
                authState.value = AuthState.Error(errorMessage)
            }
        }
    }

    fun resetAuthState() {
        authState.value = AuthState.Idle
        phoneNumber.value = ""
        name.value = ""
    }

    sealed class AuthState {
        data object Idle : AuthState()
        data object Loading : AuthState()
        data class VerificationCompleted(val user: User) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}