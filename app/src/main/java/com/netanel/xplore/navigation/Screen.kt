package com.netanel.xplore.navigation


sealed class Screen(val route: String) {
    data object SplashScreen : Screen("splash_screen")
    data object AuthScreen : Screen("auth_screen")
    data object HomeScreen : Screen("home_screen")
    data object QuizScreen : Screen("quiz_screen")
}