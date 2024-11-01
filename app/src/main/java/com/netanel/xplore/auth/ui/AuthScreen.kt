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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.R

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val phoneNumber by viewModel.phoneNumber
    val verificationCode by viewModel.verificationCode
    val verificationInProgress by viewModel.verificationInProgress
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
            Text(
                text = stringResource(R.string.to_login_write_phone_number),
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (!verificationInProgress) {
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
            } else {
                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = { viewModel.verificationCode.value = it },
                    label = { Text(stringResource(R.string.write_this_code_to_start)) },
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
        }
    }
}

