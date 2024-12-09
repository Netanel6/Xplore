package com.netanel.xplore.navigation


sealed class Screen(val route: String) {
    data object AuthScreen : Screen("auth_screen")
    data object QuizScreen : Screen("quiz_screen")
}