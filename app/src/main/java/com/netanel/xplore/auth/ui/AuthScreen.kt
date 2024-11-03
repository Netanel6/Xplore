package com.netanel.xplore.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.R
import com.netanel.xplore.auth.ui.AuthViewModel.*

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val phoneNumber by viewModel.phoneNumber
    val name by viewModel.name
    val verificationInProgress by viewModel.verificationInProgress
    val authState by viewModel.authState
    val snackbarHostState = viewModel.snackbarHostState
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
            when (authState) {
                is AuthState.Loading -> {
                    Text(
                        text = stringResource(R.string.loading),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                is AuthState.VerificationCompleted -> {
                    Text(
                        text = stringResource(R.string.to_login_write_name),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.name.value = it },
                        label = { Text(stringResource(R.string.what_is_your_name)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    Button(onClick = {
                        viewModel.onSignInButtonClicked(context)
                    }) {
                        Text(text = stringResource(R.string.shall_we))
                    }
                }
                is AuthState.SignInSuccess -> {
                    // TODO: Move to next page
                }
                is AuthState.Error -> {
                    LaunchedEffect(Unit) {
                        viewModel.resetAuthState()
                    }
                   /* Text(
                        text = (authState as AuthState.Error).message,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )*/
                }
                else -> {
                    if (!verificationInProgress) {
                        Text(
                            text = stringResource(R.string.to_login_write_phone_number),
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { viewModel.phoneNumber.value = it },
                            label = { Text(stringResource(R.string.what_is_your_number)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                        Button(onClick = {
                            viewModel.startPhoneNumberVerification(context)
                        }) {
                            Text(text = stringResource(R.string.next_step))
                        }
                    }
                }
            }
        }
    }
}


