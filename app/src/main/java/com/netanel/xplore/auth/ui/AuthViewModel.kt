package com.netanel.xplore.auth.ui

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.xplore.R
import com.netanel.xplore.auth.repository.AuthRepository
import com.netanel.xplore.auth.repository.model.User
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
    var selectedQuizId = mutableStateOf("") // New property for selected quiz ID
    val snackbarHostState = SnackbarHostState()
    var authState = mutableStateOf<AuthState>(AuthState.Idle)
        private set

    init {
        // Initialize selectedQuizId with the saved value from SharedPreferences
        selectedQuizId.value = sharedPreferencesManager.getString(SharedPrefKeys.QUIZ_ID)
    }

    fun startUserVerification(context: Context) {
        authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val user = authRepository.getUser(phoneNumber.value)

                if (user != null) {
                    saveUserDetails(user)
                    authState.value = AuthState.VerificationCompleted(user)
                } else {
                    val errorMessage = context.getString(R.string.phone_num_incorrect)
                    authState.value = AuthState.Error(errorMessage)
                    snackbarHostState.showSnackbar(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = e.message.toString()
                authState.value = AuthState.Error(errorMessage)
                snackbarHostState.showSnackbar(errorMessage)
            }
        }
    }

    private fun saveUserDetails(user: User) {
        sharedPreferencesManager.saveString(SharedPrefKeys.PHONE_NUMBER, user.phoneNumber)
        sharedPreferencesManager.saveString(SharedPrefKeys.USER_NAME, user.name)
        sharedPreferencesManager.saveBoolean(SharedPrefKeys.IS_LOGGED_IN, true)
        val initialQuizId = user.quizzes.getOrNull(0)?.id.orEmpty()
        saveSelectedQuizId(initialQuizId)
    }

    fun saveSelectedQuizId(quizId: String) {
        sharedPreferencesManager.saveString(SharedPrefKeys.QUIZ_ID, quizId)
        selectedQuizId.value = quizId
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
        data object SignInSuccess : AuthState()
        data class Error(val message: String) : AuthState()
    }
}