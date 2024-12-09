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
fun NavigationStack(
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val isLoggedInState = viewModel.isUserLoggedInFlow.collectAsState()
    val isLoggedIn = isLoggedInState.value

    val selectedQuizIdState = viewModel.selectedQuizId.collectAsState()
    val selectedQuizId = selectedQuizIdState.value

    NavHost(
        navController = navController,
        startDestination = Screen.AuthScreen.route
    ) {
        // Authentication screen
        composable(route = Screen.AuthScreen.route) {
            AuthScreen(
                onLoginSuccess = { quizId ->
                    viewModel.updateQuizId(quizId) // Save quiz ID
                    navController.navigate("${Screen.QuizScreen.route}/$quizId") {
                        // Clear the back stack to remove AuthScreen
                        popUpTo(Screen.AuthScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Quiz screen with quizId parameter
        composable(
            route = "${Screen.QuizScreen.route}/{quizId}",
            arguments = listOf(navArgument("quizId") { type = NavType.StringType })
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId").orEmpty()
            QuizScreen(quizId)
        }
    }

    // If logged in and a quizId is available, navigate dynamically
    if (isLoggedIn && selectedQuizId.isNotEmpty()) {
        navController.navigate("${Screen.QuizScreen.route}/$selectedQuizId") {
            popUpTo(Screen.AuthScreen.route) { inclusive = true }
        }
    }
}