package com.netanel.xplore.auth.ui

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthProvider
import com.netanel.xplore.R
import com.netanel.xplore.auth.repository.AuthRepository
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

    var name = mutableStateOf("")
    var verificationInProgress = mutableStateOf(false)
    private var verificationId = mutableStateOf<String?>(null)
    val snackbarHostState = SnackbarHostState()
    var authState = mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun startPhoneNumberVerification(context: Context) {
        authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val id = authRepository.startPhoneNumberVerification(
                    context.getString(R.string.israel_country_code) + phoneNumber.value
                )
                sharedPreferencesManager.saveString(SharedPrefKeys.PHONE_NUMBER, phoneNumber.value)
                authState.value = AuthState.VerificationCompleted(id)
                verificationId.value = id
                verificationInProgress.value = true
            } catch (e: FirebaseException) {
                val errorMessage = if (e.message?.contains("reCAPTCHA") == true) {
                    "reCAPTCHA failed"
                } else {
                    context.getString(R.string.phone_num_incorrect)
                }
                authState.value = AuthState.Error(errorMessage)
                viewModelScope.launch {
                    snackbarHostState.showSnackbar(errorMessage)
                }
            }
        }
    }

    fun onSignInButtonClicked(context: Context) {
        sharedPreferencesManager.saveString(SharedPrefKeys.USER_NAME, name.value)
        signInWithPhoneAuthCredential(context)
    }

    private fun signInWithPhoneAuthCredential(context: Context) {
        val credential = verificationId.value?.let {
            PhoneAuthProvider.getCredential(it, context.getString(R.string.otp))
        }

        credential?.let {
            viewModelScope.launch {
                val success = authRepository.signInWithPhoneAuthCredential(it)
                if (success) {
                    sharedPreferencesManager.saveBoolean(SharedPrefKeys.IS_LOGGED_IN, true)
                    authState.value = AuthState.SignInSuccess
                    // TODO: Move to next page
                } else {
                    authState.value = AuthState.Error(context.getString(R.string.otp_incorrect))
                    snackbarHostState.showSnackbar(context.getString(R.string.otp_incorrect))
                }
            }
        }
    }

    sealed class AuthState {
        data object Idle : AuthState()
        data object Loading : AuthState()
        data class VerificationCompleted(val verificationId: String) : AuthState()
        data object SignInSuccess : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
