package com.netanel.xplore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.netanel.xplore.MainActivityViewModel
import com.netanel.xplore.auth.ui.AuthScreen
import com.netanel.xplore.quiz.ui.QuizScreen
import com.netanel.xplore.utils.SharedPreferencesManager
@Composable
fun NavigationStack(viewModel: MainActivityViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val isLoggedInState = viewModel.isUserLoggedInFlow.collectAsState()
    val isLoggedIn = isLoggedInState.value

    NavHost(navController = navController, startDestination = if (isLoggedIn) Screen.AuthScreen.route else Screen.AuthScreen.route) {
        composable(route = Screen.AuthScreen.route) {
            AuthScreen(
                onLoginSuccess = { quizId ->
                    navController.navigate("${Screen.QuizScreen.route}/$quizId") {
                        popUpTo(Screen.AuthScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = "${Screen.QuizScreen.route}/{quizId}") { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
            QuizScreen(quizId)
        }
    }
}