package com.netanel.xplore.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.R
import com.netanel.xplore.auth.ui.AuthViewModel.AuthState
import com.netanel.xplore.utils.Logger
@Composable
fun AuthScreen(onLoginSuccess: (String?) -> Unit, viewModel: AuthViewModel = hiltViewModel()) {
    val phoneNumber by viewModel.phoneNumber
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
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated logo
            AnimatedVisibility(
                visible = authState !is AuthState.Loading,
                enter = fadeIn(animationSpec = tween(800)) + scaleIn(initialScale = 0.8f),
                exit = fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 0.8f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "App logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp)
                )
            }

            // Title
            AnimatedVisibility(
                visible = authState !is AuthState.Loading,
                enter = slideInVertically(initialOffsetY = { -50 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -50 }) + fadeOut()
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (authState) {
                        is AuthState.Loading -> {
                            LoadingAnimation()
                        }
                        is AuthState.VerificationCompleted -> {
                            // Trigger navigation to HomeScreen and reset state
                            LaunchedEffect(Unit) {
                                val userId = (authState as AuthState.VerificationCompleted).user.id

                                onLoginSuccess(userId)
                                viewModel.resetAuthState()
                            }
                        }
                        is AuthState.Error -> {
                            val errorMessage = (authState as AuthState.Error).message
                            ErrorMessage(errorMessage)

                            Spacer(modifier = Modifier.height(16.dp))

                            // Retry Button
                            Button(
                                onClick = { viewModel.resetAuthState() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(text = stringResource(R.string.retry))
                            }
                        }
                        else -> {
                            Text(
                                text = stringResource(R.string.to_login_write_phone_number),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { viewModel.phoneNumber.value = it },
                                label = { Text(stringResource(R.string.what_is_your_number)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                shape = RoundedCornerShape(8.dp)
                            )
                            Button(
                                onClick = { viewModel.startUserVerification(context) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(text = stringResource(R.string.next_step))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingAnimation() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.loading),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.error
    )
}
