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
fun AuthScreen(onLoginSuccess: () -> Unit, viewModel: AuthViewModel = hiltViewModel()) {
    val phoneNumber by viewModel.phoneNumber
    val name by viewModel.name
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
                    val user = (authState as AuthState.VerificationCompleted).user
                    Text(
                        text = stringResource(R.string.welcome_back, user.name),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = { onLoginSuccess() }) {
                        Text(text = stringResource(R.string.continue_to_dashboard))
                    }
                }
                is AuthState.Error -> {
                    val errorMessage = (authState as AuthState.Error).message
                    Text(
                        text = errorMessage,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = { viewModel.resetAuthState() }) {
                        Text(text = stringResource(R.string.try_again))
                    }
                }
                else -> {
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
                        viewModel.startUserVerification(context)
                    }) {
                        Text(text = stringResource(R.string.next_step))
                    }
                }
            }
        }
    }
}