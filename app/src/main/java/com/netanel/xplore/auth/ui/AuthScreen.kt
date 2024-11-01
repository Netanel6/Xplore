package com.netanel.xplore.auth.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.netanel.xplore.R
import com.netanel.xplore.utils.Logger
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun AuthScreen(auth: FirebaseAuth, viewModel: AuthViewModel = hiltViewModel()) {
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var verificationInProgress by remember { mutableStateOf(false) }
    var verificationId by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.to_login_write_phone_number),
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (!verificationInProgress) {
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text(stringResource(R.string.what_is_your_number)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                Button(onClick = {
                    verificationInProgress = true
                    startPhoneNumberVerification(context, auth,
                        context.getString(R.string.israel_country_code).plus(phoneNumber),
                        onVerificationComplete = {
                            verificationId = it
                        },
                        onVerificationFailed = { errorMessage ->
                            verificationInProgress = false
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(errorMessage)
                            }
                        }
                    )
                }) {
                    Text(text = stringResource(R.string.next_step))
                }
            } else {
                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    label = { Text(stringResource(R.string.write_this_code_to_start)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                Button(onClick = {
                    verificationId?.let {
                        val credential = PhoneAuthProvider.getCredential(it, verificationCode)
                        signInWithPhoneAuthCredential(context, auth, credential,
                            onSignInFailed = { errorMessage ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(errorMessage)
                                }
                            }
                        )
                    }
                }) {
                    Text(text = stringResource(R.string.shall_we))
                }
            }
        }
    }
}

fun startPhoneNumberVerification(
    context: Context,
    auth: FirebaseAuth,
    phoneNumber: String,
    onVerificationComplete: (String) -> Unit,
    onVerificationFailed: (String) -> Unit
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Logger.info("PhoneAuth", "Verification completed.")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Logger.info("PhoneAuth", "Verification failed.\n$e")
                onVerificationFailed(context.getString(R.string.phone_num_incorrect))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {;
                Logger.info("PhoneAuth", "Code sent: $verificationId")
                onVerificationComplete(verificationId)
            }
        })
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}

private fun signInWithPhoneAuthCredential(
    context: Context,
    auth: FirebaseAuth,
    credential: PhoneAuthCredential,
    onSignInFailed: (String) -> Unit
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Logger.info("PhoneAuth", "Sign-in successful.")
            } else {
                Logger.info("PhoneAuth", "Sign-in failed.\n${task.exception}")
                onSignInFailed(context.getString(R.string.otp_incorrect))
            }
        }
}
