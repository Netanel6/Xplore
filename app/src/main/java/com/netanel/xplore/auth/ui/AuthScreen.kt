package com.netanel.xplore.auth.ui

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.netanel.xplore.R
import com.netanel.xplore.auth.ui.AuthViewModel.AuthState
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.ui.AnimatedComposable
import com.netanel.xplore.ui.theme.OnPrimary

@Composable
fun AuthScreen(
    onLoginSuccess: (String) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }
    val authState by authViewModel.authState
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.VerificationCompleted -> {
                val user = (authState as AuthState.VerificationCompleted).user
                userViewModel.saveUser(user)
                onLoginSuccess(user.id.toString())
            }

            is AuthState.Error -> {
                isLoading = false
                val errorMessage = (authState as AuthState.Error).message
                Log.d("AuthScreen", "Error: $errorMessage")
            }

            else -> {}
        }
    }

    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.Asset("login_animation.json"))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Use background color
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp), // More horizontal padding
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Lottie Animation
            LottieAnimation(
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(250.dp) //Larger
            )

            Spacer(modifier = Modifier.height(60.dp))


            // Input and Button (No Card)
            AuthInputAndButton(
                phoneNumber = phoneNumber.trim(),
                onPhoneNumberChange = { phoneNumber = it },
                isLoading = isLoading,
                onButtonClick = {
                    isLoading = true
                    authViewModel.startUserVerification(phoneNumber)
                }
            )


            //Error message
            AnimatedComposable(
                isVisible = authState is AuthState.Error,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(300)),
                content = {
                    val errorMessage = (authState as? AuthState.Error)?.message
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp) // Add some padding
                        )
                    }
                }
            )
        }

        // Loading Overlay (Full Screen)
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent black
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = OnPrimary) // Use your app's primary color
            }
        }
    }
}


@Composable
fun AuthInputAndButton(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isLoading: Boolean,
    onButtonClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current // To dismiss keyboard

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between input and button
    ) {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = {
                Text(
                    stringResource(R.string.to_login_write_phone_number),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (phoneNumber.isNotBlank()) {
                        onButtonClick()
                    }
                }
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "Phone Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            colors = TextFieldDefaults.colors()
        )

        Button(
            onClick = {
                focusManager.clearFocus()
                onButtonClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = phoneNumber.isNotBlank() && !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = OnPrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    stringResource(R.string.next_step),
                    fontSize = 18.sp
                )
            }
        }
    }
}