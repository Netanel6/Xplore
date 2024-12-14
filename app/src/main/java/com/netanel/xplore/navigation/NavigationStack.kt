package com.netanel.xplore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.netanel.xplore.MainActivityViewModel
import com.netanel.xplore.auth.ui.AuthScreen
import com.netanel.xplore.quiz.ui.QuizScreen

@Composable
fun NavigationStack(viewModel: MainActivityViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val isLoggedInState = viewModel.isUserLoggedInFlow.collectAsState()
    val isLoggedIn = isLoggedInState.value

    val selectedQuizState = viewModel.selectedQuiz.collectAsState()
    val selectedQuiz = selectedQuizState.value

    val startDestination = if (isLoggedIn && selectedQuiz.isNotEmpty()) {
        "${Screen.QuizScreen.route}/$selectedQuiz"
    } else {
        Screen.AuthScreen.route
    }

    NavHost(
        navController = navController,
        startDestination = Screen.AuthScreen.route
    ) {

        composable(route = Screen.AuthScreen.route) {
            AuthScreen(
                onLoginSuccess = { quizId ->
                    navController.navigate("${Screen.QuizScreen.route}/$quizId") {
                        popUpTo(Screen.AuthScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Screen.QuizScreen.route}/{quizId}",
            arguments = listOf(navArgument("quizId") { type = NavType.StringType })
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
            QuizScreen(quizId)
        }
    }

    if (isLoggedIn && selectedQuiz.isNotEmpty()) {
        navController.navigate("${Screen.QuizScreen.route}/$selectedQuiz") {
            popUpTo(Screen.AuthScreen.route) { inclusive = true }
        }
    }
}
