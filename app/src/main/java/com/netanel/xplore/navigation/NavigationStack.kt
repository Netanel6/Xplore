package com.netanel.xplore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.netanel.xplore.auth.ui.AuthScreen
import com.netanel.xplore.home.HomeScreen

import com.netanel.xplore.quiz.ui.QuizScreen


@Composable
fun NavigationStack() {
    val navController = rememberNavController()
    // TODO: Fix logic on boolean to get to home screen when logged it
    val isLoggedIn = false

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn)"${Screen.HomeScreen.route}/{userId}" else Screen.AuthScreen.route
    ) {
        composable(route = Screen.AuthScreen.route) {
            AuthScreen(
                onLoginSuccess = { userId ->
                    navController.navigate("${Screen.HomeScreen.route}/$userId") {
                        popUpTo(Screen.AuthScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Screen.HomeScreen.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")

            if (userId?.isNotEmpty() == true) {
                HomeScreen(
                    userId = userId,
                    onQuizSelected = { _userId, quizId ->
                        navController.navigate("${Screen.QuizScreen.route}/$_userId/$quizId")
                    }
                )
            } else {
                navController.navigate(Screen.AuthScreen.route) {
                    popUpTo(Screen. HomeScreen.route) { inclusive = true }
                }
            }
        }

        composable(
            route = "${Screen.QuizScreen.route}/{userId}/{quizId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("quizId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
            QuizScreen(userId = userId, quizId = quizId)
        }
    }
}
