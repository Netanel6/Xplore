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

@Composable
fun NavigationStack(viewModel: MainActivityViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val isLoggedInState = viewModel.isUserLoggedInFlow.collectAsState()
    val isLoggedIn = isLoggedInState.value

    val startDestination = if (isLoggedIn) {
        Screen.QuizScreen.route
    } else {
        Screen.AuthScreen.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.AuthScreen.route) {
            AuthScreen(
                onLoginSuccess = {
                    viewModel.updateUserLoginStatus(true)
                    navController.navigate(Screen.QuizScreen.route) {
                        popUpTo(Screen.AuthScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screen.QuizScreen.route) {
            QuizScreen()
        }
    }
}