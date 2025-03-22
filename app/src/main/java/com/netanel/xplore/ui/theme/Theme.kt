package com.netanel.xplore.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
    primary = PaleBlue,
    onPrimary = SoftWhite,
    primaryContainer = MediumGray,
    onPrimaryContainer = SoftWhite,
    secondary = NeutralGray,
    onSecondary = SoftWhite, // ADDED THIS
    tertiary = LightCream,
    onTertiary = DarkerGray,
    background = BackgroundDark,
    onBackground = SoftWhite,
    surface = BackgroundDark,
    onSurface = SoftWhite,
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = NeutralGray, // Use a lighter gray for contrast
    error = Error, // Use custom error color
    onError = Color.White, // White text on error background
    outline = MediumGray, // Outline/border color
    inverseOnSurface = DarkerGray, //For snackbars and such.
    inverseSurface = SoftWhite,
    surfaceTint = md_theme_dark_surfaceTint,
    scrim = md_theme_dark_scrim, //Semi transparent black.
    outlineVariant = md_theme_dark_outlineVariant
)

// Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = PaleBlue,
    onPrimary = Color.White,
    primaryContainer = SoftBeige,
    onPrimaryContainer = DarkerGray,
    secondary = NeutralGray,
    onSecondary = Color.Black, // ADDED THIS: Use Black for good contrast
    tertiary = LightCream,
    onTertiary = Color.Black, // Use a clear contrasting color.
    background = BackgroundLight,
    onBackground = Color.Black,
    surface = BackgroundLight,
    onSurface = Color.Black,
    surfaceVariant = SoftWhite, // Use a slightly different shade
    onSurfaceVariant = MediumGray, // Use a gray for contrast on surfaceVariant
    error = Error,
    onError = Color.White,
    outline = NeutralGray, // Outline/border color
    inverseOnSurface = SoftWhite,
    inverseSurface = DarkerGray,
    surfaceTint = md_theme_light_surfaceTint,
    scrim = md_theme_light_scrim,
    outlineVariant = md_theme_light_outlineVariant,
)

@Composable
fun XploreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // You should define your Typography
        content = content
    )
}