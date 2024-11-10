package com.netanel.xplore.navigation


sealed class Screen(val route: String) {
    object AuthScreen : Screen("auth_screen")
    object QuizScreen : Screen("quiz_screen")
}