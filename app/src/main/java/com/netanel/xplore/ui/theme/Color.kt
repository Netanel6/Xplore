// ui/theme/Color.kt
package com.netanel.xplore.ui.theme

import androidx.compose.ui.graphics.Color

// 🎨 Primary Colors
val SoftWhite = Color(0xFFFAF9F6)  // Use for light backgrounds/surfaces
val NeutralGray = Color(0xFFD6D6D6) // Light gray, good for unselected/disabled states
val SoftBeige = Color(0xFFF5E7DA)   // Light, warm color. Good for containers.

// 🎨 Supporting Colors
val MediumGray = Color(0xFFBDBDBD) // Good for borders, secondary elements
val LightCream = Color(0xFFFFF5E1)  // Another light, warm color

//val DarkerGray = Color(0xFFFFFFFF)  // This was white! Changed to a dark gray.
val DarkerGray = Color(0xFF5A5A5A) // Reusing your OnSecondary color
val PaleBlue = Color(0xFF6A89CC)    // Primary color

// 🎨 Background Colors
val BackgroundLight = SoftWhite
val BackgroundDark = Color(0xFF2D2D2D)

// 🎨 Text Colors (Now mainly used as *defaults* before being assigned to roles)
val OnPrimary = Color(0xff0b42a3)  // Use this as your primary *brand* color
//val OnPrimarySecond = Color(0xff3b42a3) // No longer needed

// 🎨 Gradient Colors
val GradientStart = Color(0xFF6A11CB)
val GradientMid = Color(0xFF2575FC)
val GradientEnd = Color(0xFF4285F4)

// 🎨 Answer Colors (Keep these separate for now, we'll integrate them)
val AnswerUnselected = NeutralGray
val AnswerBorder = MediumGray
val Correct = Color(0xFF4CAF50) // Standard green
val Error = Color(0xFFB00020)   // Standard red

//Additional colors:

val md_theme_light_surfaceTint = Color(0xFF6650a4)
val md_theme_light_scrim = Color(0xFF000000)
val md_theme_light_surface = Color(0xFFFFFBFE)
val md_theme_light_outlineVariant = Color(0xFFCAC4D0)

val md_theme_dark_surfaceTint = Color(0xFFCFBCFF)
val md_theme_dark_scrim = Color(0xFF000000)
val md_theme_dark_outlineVariant = Color(0xFF49454F)
val md_theme_dark_surface = Color(0xFF1C1B1F)