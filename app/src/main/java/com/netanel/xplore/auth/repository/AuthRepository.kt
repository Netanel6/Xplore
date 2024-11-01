package com.netanel.xplore.auth.repository

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.netanel.xplore.utils.Logger
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Created by netanelamar on 01/11/2024.
 * NetanelCA2@gmail.com
 */
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun startPhoneNumberVerification(phoneNumber: String): String {
        return suspendCancellableCoroutine { continuation ->
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        Logger.info("PhoneAuth", "Verification completed.")
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Logger.info("PhoneAuth", "Verification failed.\n$e")
                        continuation.resumeWithException(e)
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        Logger.info("PhoneAuth", "Code sent: $verificationId")
                        continuation.resume(verificationId)
                    }
                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Boolean {
        return suspendCancellableCoroutine { continuation ->
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.info("PhoneAuth", "Sign-in successful.")
                        continuation.resume(true)
                    } else {
                        Logger.info("PhoneAuth", "Sign-in failed.\n${task.exception}")
                        continuation.resume(false)
                    }
                }
        }
    }
}
