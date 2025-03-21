package com.netanel.xplore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.netanel.xplore.ui.theme.OnPrimary

@Composable
fun SplashScreen(isLoggedIn: Boolean) {
    val (backgroundColor, progressColor) = getSplashScreenColors(isLoggedIn)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        CircularProgressIndicator(color = progressColor)
    }
}

@Composable
fun getSplashScreenColors(isLoggedIn: Boolean): Pair<Color, Color> {
    return if (isLoggedIn) {
        MaterialTheme.colorScheme.onSurface to OnPrimary
    } else {
        OnPrimary to MaterialTheme.colorScheme.onSurface
    }
}
