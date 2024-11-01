package com.netanel.xplore.auth.ui

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.netanel.xplore.R
import com.netanel.xplore.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by netanelamar on 01/11/2024.
 * NetanelCA2@gmail.com
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    var phoneNumber = mutableStateOf("")
    var verificationCode = mutableStateOf("")
    var verificationInProgress = mutableStateOf(false)
    var verificationId = mutableStateOf<String?>(null)
    val snackbarHostState = SnackbarHostState()

    fun startPhoneNumberVerification(context: Context) {
        verificationInProgress.value = true
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(context.getString(R.string.israel_country_code).plus(phoneNumber.value))
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Logger.info("PhoneAuth", "Verification completed.")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Logger.info("PhoneAuth", "Verification failed.\n$e")
                    verificationInProgress.value = false
                    viewModelScope.launch {
                        snackbarHostState.showSnackbar(context.getString(R.string.phone_num_incorrect))
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Logger.info("PhoneAuth", "Code sent: $verificationId")
                    this@AuthViewModel.verificationId.value = verificationId
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun onSignInButtonClicked(context: Context) {
        if (verificationCode.value.isNotBlank()) {
            signInWithPhoneAuthCredential(context)
        } else {
            viewModelScope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.otp_incorrect))
            }
        }
    }

    fun signInWithPhoneAuthCredential(context: Context) {
        val credential = verificationId.value?.let {
            PhoneAuthProvider.getCredential(it, verificationCode.value)
        }

        credential?.let {
            auth.signInWithCredential(it)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.info("PhoneAuth", "Sign-in successful.")
                    } else {
                        Logger.info("PhoneAuth", "Sign-in failed.\n${task.exception}")
                        viewModelScope.launch {
                            snackbarHostState.showSnackbar(context.getString(R.string.otp_incorrect))
                        }
                    }
                }
        }
    }
}
