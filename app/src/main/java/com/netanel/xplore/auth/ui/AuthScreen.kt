package com.netanel.xplore.auth.ui

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.R
import com.netanel.xplore.auth.ui.AuthViewModel.AuthState
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.ui.AnimatedComposable
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
    onLoginSuccess: (String) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    var phoneNumber by remember { mutableStateOf("") }
    val authState by authViewModel.authState
    var isLoading by remember { mutableStateOf(false) }
    var isUiVisible by remember { mutableStateOf(false) }

    // 🚀 UI Fade-in Animation
    LaunchedEffect(Unit) {
        delay(500) // Delay for smooth appearance
        isUiVisible = true
    }

    // 🎯 Handle Authentication State
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.VerificationCompleted -> {
                val user = (authState as AuthState.VerificationCompleted).user
                userViewModel.saveUser(user)
                onLoginSuccess(user.id.toString())
            }

            is AuthState.Error -> {
                isLoading = false
                Log.d("AuthScreen", "Error: ${(authState as AuthState.Error).message}")
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // 🎬 Animated UI
        AnimatedComposable(
            isVisible = isUiVisible,
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(animationSpec = tween(700)),
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 🌟 App Logo
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 📌 App Title
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🔐 Auth Input Card
                    AuthInputCard(
                        phoneNumber = phoneNumber,
                        onPhoneNumberChange = { phoneNumber = it },
                        authState = authState,
                        isLoading = isLoading,
                        onButtonClick = {
                            isLoading = true
                            authViewModel.startUserVerification(phoneNumber)
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun AuthInputCard(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    authState: AuthState,
    isLoading: Boolean,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 📱 Phone Input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = { Text(stringResource(R.string.what_is_your_number)) },
                modifier = Modifier.fillMaxWidth()
            )

            // 🔄 Login Button
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = phoneNumber.isNotBlank() && authState != AuthState.Loading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.next_step))
                }
            }

            // ❌ Animated Error Message
            AnimatedComposable(
                isVisible = authState is AuthState.Error,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)),
                content = {
                    val errorMessage = (authState as? AuthState.Error)?.message
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        }
    }
}
